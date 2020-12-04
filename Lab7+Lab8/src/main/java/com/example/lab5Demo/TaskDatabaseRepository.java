package com.example.lab5Demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository("database")
public class TaskDatabaseRepository implements TaskRepository{
    private NamedParameterJdbcTemplate template;

    @Autowired
    public void setDataSource(final DataSource dataSource){
        template = new NamedParameterJdbcTemplate(dataSource);
    }

    public final RowMapper<TaskModel> mapper = new RowMapper<TaskModel>() {
        @Override
        public TaskModel mapRow(ResultSet resultSet, int i) throws SQLException {
            return new TaskModel(resultSet.getString("id"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("assignedTo"),
                    TaskModel.TaskStatus.valueOf(resultSet.getString("status")),
                            TaskModel.TaskSeverity.valueOf(resultSet.getString("severity")));
        }
    };


    @Override
    public Collection<TaskModel> findAll() {
        return template.query("select id, title, description, assignedto, status, severity from task", mapper);
    }

    @Override
    public Optional<TaskModel> findById(String id) {
        TaskModel task = template.queryForObject(
                "select id, title, description, assignedto, status, severity from task where id= :id",
                new MapSqlParameterSource().addValue("id", id), mapper);
        return task != null ? Optional.of(task) : Optional.empty();
    }

    @Override
    public void save(TaskModel task) throws IOException {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", task.getId())
                .addValue("title", task.getTitle())
                .addValue("description", task.getDescription())
                .addValue("assignedTo", task.getAssignedTo())
                .addValue("status", task.getStatus().name())
                .addValue("severity", task.getSeverity().name());

        int count = template.update("update task set title =:title, description= :description, status=:status," +
                " severity= :severity where id =:id", parameters);
        if(count == 0){
            template.update("insert into task (id, title, description, assignedTo, status, severity) "+
                    "values (:id, :title, :description, :assignedTo, :status, :severity)", parameters);
        }
    }

    @Override
    public void deleteById(String id) throws IOException {
        template.update("delete from task where id=:id",
                new MapSqlParameterSource().addValue("id", id));
    }
}
