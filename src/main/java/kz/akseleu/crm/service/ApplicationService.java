package kz.akseleu.crm.service;

import kz.akseleu.crm.model.Application;
import kz.akseleu.crm.model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicationService {
    private final Connection connection;

    public List<Application> getAllApplication() {
        String sql = """
                    SELECT a.id, a.user_name, a.commentary, a.phone, a.handled,
                           c.id AS course_id, c.name AS course_name, c.price AS course_price
                    FROM t_application a
                             JOIN t_course c ON c.id = a.course_id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet st = statement.executeQuery()) {

            List<Application> applications = new ArrayList<>();
            while (st.next()) {
                applications.add(mapRow(st));
            }
            return applications;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Application getById(Long id) {
        String sql = """
                    SELECT a.id, a.user_name, a.commentary, a.phone, a.handled,
                           c.id AS course_id, c.name AS course_name, c.price AS course_price
                    FROM t_application a
                    JOIN t_course c ON c.id = a.course_id
                    WHERE a.id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet st = statement.executeQuery()) {
                if (!st.next()) return null;
                return mapRow(st);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // controller вызывает save() для create и update
    public boolean save(Application application) {
        if (application.getId() == null) {
            return insert(application);
        }
        return update(application);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM t_application WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean insert(Application application) {
        String sql = """
                    INSERT INTO t_application (user_name, course_id, commentary, phone, handled)
                    VALUES (?,?,?,?,?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, application.getUser_name());
            statement.setLong(2, application.getCourse().getId()); // <-- Course ID
            statement.setString(3, application.getCommentary());
            statement.setString(4, application.getPhone());
            statement.setBoolean(5, application.isHandled());
            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean update(Application application) {
        String sql = """
                    UPDATE t_application SET
                    user_name = ?, course_id = ?, commentary = ?, phone = ?, handled = ?
                    WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, application.getUser_name());
            statement.setLong(2, application.getCourse().getId()); // <-- Course ID
            statement.setString(3, application.getCommentary());
            statement.setString(4, application.getPhone());
            statement.setBoolean(5, application.isHandled());
            statement.setLong(6, application.getId());
            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Application mapRow(ResultSet st) throws SQLException {
        Course course = new Course();
        course.setId(st.getLong("course_id"));
        course.setName(st.getString("course_name"));
        course.setPrice(st.getInt("course_price"));


        Application a = new Application();
        a.setId(st.getLong("id"));
        a.setUser_name(st.getString("user_name"));
        a.setCommentary(st.getString("commentary"));
        a.setPhone(st.getString("phone"));
        a.setHandled(st.getBoolean("handled"));
        a.setCourse(course); // <-- ВАЖНО: ставим объект Course

        return a;
    }
}