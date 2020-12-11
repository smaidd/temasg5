package com.example.lab9Demo;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class MeetingRepository {
    private final NamedParameterJdbcTemplate template;

    public MeetingRepository(final DataSource dataSource){
        template = new NamedParameterJdbcTemplate(dataSource);
    }

    private static final RowMapper<MeetingModel> meetingMapper = new RowMapper<MeetingModel>() {
        @Override
        public MeetingModel mapRow(ResultSet rs, int rowNum) throws SQLException {
           return new MeetingModel(rs.getString("id"),
                   rs.getString("subject"),
                   rs.getString("description"),
                   rs.getString("room_id"),
                   rs.getTimestamp("start").toLocalDateTime(),
                   rs.getTimestamp("end").toLocalDateTime());
        }
    };
    private static final RowMapper<String> attendeesMapper = new RowMapper<String>() {
        @Override
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString("contact_id");
        }
    };
    public List<MeetingModel> getMeetings(LocalDateTime start, LocalDateTime end, String roomId){
        String sql = "select id, subject, description, start, end, room_id from meetings ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        if(start != null && end != null){
            sql += " where start <= :end and end >= :start ";
            parameters.addValue("start", start).addValue("end", end);
            if(roomId != null){
                sql += " and room_id = :room_id";
                parameters.addValue("room_id", roomId);
            }
        }
        else if(roomId != null){
            sql += " where room_id = :room_id";
            parameters.addValue("room_id", roomId);
        }
        sql += " order by start";

        return template.query(sql, parameters, meetingMapper);
    }

    public void saveMeeting(MeetingModel meetingModel){
        if(getMeetings(meetingModel.getStart(), meetingModel.getEnd(), meetingModel.getRoomId())
        .stream().filter(other -> !other.getId().equals(meetingModel.getId()))
                .findAny()
                .isPresent()
        ){
            throw new MeetingException();
        }

        String updateSql = "update meetings set "
                + "subject = :subject, "
                + "description = :description, "
                + "room_id = :room_id, "
                + "start = :start,"
                + "end = :end "
                + "where id = :id";
        String insertSql = "insert into meetings (id, subject, description, room_id, start, end) "
                + "values (:id, :subject, :description, :room_id, :start, :end) ";

        MapSqlParameterSource parameters  = new MapSqlParameterSource()
                .addValue("id", meetingModel.getId())
                .addValue("subject", meetingModel.getSubject())
                .addValue("description", meetingModel.getDescription())
                .addValue("start", meetingModel.getStart())
                .addValue("end", meetingModel.getEnd())
                .addValue("room_id", meetingModel.getRoomId());

        if(template.update(updateSql, parameters) != 1){
            template.update(insertSql, parameters);
        }
    }

    public void removeMeeting(String id){
        String sql = "delete from meetings where id =:id";
        MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("id", id);
        if(template.update(sql, parameters) != 1){
            throw new MeetingException();
        }
    }

    public List<String> getAttendees (String meetingId){
        String sql = "select contact_id from attendees where meeting_id =:meeting_id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("meeting_id", meetingId);
        return template.query(sql, params, attendeesMapper);
    }
    public void addAttendee (String meetingId, String contactId){
        String sql = "insert into attendees (id, meeting_id, contact_id) values (:id, :meeting_id, :contact_id)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", UUID.randomUUID().toString())
                .addValue("meeting_id", meetingId)
                .addValue("contact_id", contactId);
        if(template.update(sql, params) != 1){
            throw new MeetingException();
        }
    }
    public void removeAttendee (String meetingId, String contactId){
        String sql = "delete from attendees where meeting_id = :meeting_id and contact_id =:contact_id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("meeting_id", meetingId)
                .addValue("contact_id", contactId);
        if(template.update(sql, params) != 1){
            throw new MeetingException();
        }
    }

}
