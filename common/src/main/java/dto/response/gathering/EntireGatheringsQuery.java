package dto.response.gathering;

import java.time.LocalDateTime;


public interface EntireGatheringsQuery {

    Long getId();
    String getTitle();
    String getContent();
    LocalDateTime getRegisterDate();
    String getCategory();
    Long getCreatedById();
    Long getImageId();
    int getCount();
}
