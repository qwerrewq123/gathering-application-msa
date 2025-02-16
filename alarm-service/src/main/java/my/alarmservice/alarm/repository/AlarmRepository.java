package my.alarmservice.alarm.repository;

import my.alarmservice.alarm.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlarmRepository extends JpaRepository<Alarm,Long> {
    @Query("select a from Alarm a where a.userId = :userId and a.checked = true")
    Page<Alarm> fetchCheckedAlarmPage(PageRequest pageRequest, Long userId);

    @Query("select a from Alarm a where a.userId = :userId and a.checked = false")
    Page<Alarm> fetchUnCheckedAlarmPage(PageRequest pageRequest, Long userId);
}
