package gathering.msa.gathering.repository;

import dto.response.gathering.EntireGatheringsQuery;
import dto.response.gathering.GatheringDetailQuery;
import dto.response.gathering.GatheringResponseQuery;
import dto.response.gathering.GatheringsQuery;
import gathering.msa.gathering.entity.Gathering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;


public interface GatheringRepository extends JpaRepository<Gathering, Long> {
    @Query("select " +
            "new dto.response.gathering." +
            "GatheringDetailQuery(g.id,g.title,g.content,g.registerDate,ca.name,g.userId,e.id,g.imageId,gc.count) " +
            "from Gathering g " +
            "left join Enrollment e on e.gathering.id=g.id " +
            "left join g.category ca " +
            "left join GatheringCount gc on gc.gathering.id = g.id " +
            "where g.id = :gatheringId")
    List<GatheringDetailQuery> gatheringDetail(Long gatheringId);

    @Query("select new dto.response.gathering." +
            "GatheringsQuery(g.id,g.title,g.content,g.registerDate,ca.name,g.userId,g.imageId,g.count) " +
            "from Gathering  g " +
            "left join  g.category ca " +
            "left join GatheringCount gc on gc.gathering.id = g.id " +
            "where ca.name =:category"
    )
    Page<GatheringsQuery> gatheringsCategory(PageRequest pageRequest, String category);
    @Query(value = "select id,title,content,registerDate,category,createdBy,url,count from ( " +
            "  select g.id as id, g.title as title, g.content as content, g.register_date as registerDate, ca.name as category, " +
            "         g.user_id as createdById, g.image_id as imageId, gc.count as count, " +
            "         row_number() over (partition by ca.id order by g.count desc) as rownum " +
            "  from gathering g " +
            "  left join category ca on g.category_id = ca.id " +
            "left join GatheringCount gc on gc.gathering.id = g.id " +
            "  where g.title like concat('%', :title, '%') " +
            ") as subquery " +
            "where rownum between 1 and 9", nativeQuery = true)
    List<EntireGatheringsQuery> gatherings(@Param("title") String title);
    @Query("select new dto.response.gathering.GatheringResponseQuery(g.id,g.title,g.content,g.registerDate,c.name,g.userId,g.imageId,gc.count) " +
            "from Gathering g left join g.category c left join GatheringCount  gc " +
            "where g.id in :gatheringIds")
    Page<GatheringResponseQuery> findByIds(List<Long> gatheringIds);
}
