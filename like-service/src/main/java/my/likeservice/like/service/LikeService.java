package my.likeservice.like.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import my.likeservice.client.GatheringServiceClient;
import my.likeservice.client.UserServiceClient;
import my.likeservice.dto.response.DislikeResponse;
import my.likeservice.dto.response.GatheringResponse;
import my.likeservice.dto.response.LikeResponse;
import my.likeservice.dto.response.UserResponse;
import my.likeservice.exception.AlreadyLikeException;
import my.likeservice.exception.NoFoundGatheringException;
import my.likeservice.exception.NoFoundLikeException;
import my.likeservice.exception.NoFoundUserException;
import my.likeservice.like.Like;
import my.likeservice.like.repository.LikeRepository;
import my.likeservice.util.LikeConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import static my.likeservice.util.LikeConst.*;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserServiceClient userServiceClient;
    private final GatheringServiceClient gatheringServiceClient;
    @Value("${secret.key}")
    private String secretKey;

    public LikeResponse like(Long gatheringId, String token) {

        try {
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            if(!userResponse.getCode().equals("SU")){
                throw new NoFoundUserException("no User!");
            }
            GatheringResponse gatheringResponse = gatheringServiceClient.gatheringDetail(gatheringId, token);
            if(!gatheringResponse.getCode().equals("SU")){
                throw new NoFoundGatheringException("No gathering!");
            }
            Like like = likeRepository.fetchLike(userResponse.getId(), gatheringId).orElse(null);
            if(like != null){
                throw new AlreadyLikeException("already liked!");
            }

            likeRepository.save(Like.builder()
                    .gathering_id(gatheringId)
                    .likedBy(userResponse.getId())
                    .build());

            return LikeResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NoFoundUserException e){
            return LikeResponse.builder()
                    .code(noFoundUserCode)
                    .message(noFoundUserMessage)
                    .build();
        }
        catch (NoFoundGatheringException e){
            return LikeResponse.builder()
                    .code(noFoundGatheringCode)
                    .message(noFoundGatheringMessage)
                    .build();
        }
        catch (AlreadyLikeException e){
            return LikeResponse.builder()
                    .code(alreadyLikeCode)
                    .message(alreadyLikeMessage)
                    .build();
        }
        catch (Exception e){
            return LikeResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();
        }






    }

    public DislikeResponse dislike(Long gatheringId, String token) {
        try {
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            if(!userResponse.getCode().equals("SU")){
                throw new NoFoundUserException("no User!");
            }

            GatheringResponse gatheringResponse = gatheringServiceClient.gatheringDetail(gatheringId, token);
            if(!gatheringResponse.getCode().equals("SU")){
                throw new NoFoundGatheringException("No gathering!");
            }

            Like like = likeRepository.fetchLike(userResponse.getId(), gatheringId).orElseThrow(()-> new NoFoundLikeException("No Like!!"));

            likeRepository.delete(like);
            return DislikeResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();
        }catch (NoFoundUserException e){
            return DislikeResponse.builder()
                    .code(noFoundUserCode)
                    .message(noFoundUserMessage)
                    .build();
        }
        catch (NoFoundGatheringException e){
            return DislikeResponse.builder()
                    .code(noFoundGatheringCode)
                    .message(noFoundGatheringMessage)
                    .build();
        }
        catch (Exception e){
            return DislikeResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();
        }
    }

    private String getUsername(String token){
        Claims claims = null;
        String subject =null;

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)) ;


        try{

            claims = Jwts.parserBuilder().setSigningKey(key)
                    .build()
                    .parseClaimsJws(token).getBody();

            subject = claims.getSubject();


        } catch(Exception exception){
            exception.printStackTrace();
            return null;

        }

        return subject;

    }
}
