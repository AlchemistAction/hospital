package net.thumbtack.school.hospital.mybatis.mappers;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Commission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommissionAppointmentMapper {

    @Insert({"<script>",
            "INSERT INTO commission_appointment (commission_id, appointment_id) VALUES",
            "<foreach item='item' collection='list' separator=','>",
            "( #{commission.id}, #{item.id})",
            "</foreach>",
            "</script>"})
    void batchInsert(@Param("commission") Commission commission, @Param("list") List<Appointment> appointmentList);

}
