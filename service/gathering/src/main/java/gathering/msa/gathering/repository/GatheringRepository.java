package gathering.msa.gathering.repository;

import dto.response.gathering.EntireGatheringsQuery;
import dto.response.gathering.GatheringDetailQuery;
import dto.response.gathering.GatheringsQuery;
import gathering.msa.gathering.entity.Gathering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GatheringRepository extends JpaRepository<Gathering, Long> {
    @Query("select " +
            "new dto.response.gathering." +
            "GatheringDetailQuery(g.id,g.title,g.content,g.registerDate,ca.name,g.userId,e.id,g.imageId,g.count) " +
            "from Gathering g " +
            "left join Enrollment e on e.gathering.id=g.id " +
            "left join g.category ca " +
            "where g.id = :gatheringId")
    List<GatheringDetailQuery> gatheringDetail(Long gatheringId);

    @Query("select new dto.response.gathering." +
            "GatheringsQuery(g.id,g.title,g.content,g.registerDate,ca.name,g.userId,g.imageId,g.count) " +
            "from Gathering  g " +
            "left join  g.category ca " +
            "where ca.name =:category"
    )
    Page<GatheringsQuery> gatheringsCategory(PageRequest pageRequest, String category);
    @Query(value = "select id,title,content,registerDate,category,createdBy,url,count from ( " +
            "  select g.id as id, g.title as title, g.content as content, g.register_date as registerDate, ca.name as category, " +
            "         g.user_id as createdById, g.image_id as imageId, g.count as count, " +
            "         row_number() over (partition by ca.id order by g.count desc) as rownum " +
            "  from gathering g " +
            "  left join category ca on g.category_id = ca.id " +
            "  where g.title like concat('%', :title, '%') " +
            ") as subquery " +
            "where rownum between 1 and 9", nativeQuery = true)
    List<EntireGatheringsQuery> gatherings(@Param("title") String title);
}
