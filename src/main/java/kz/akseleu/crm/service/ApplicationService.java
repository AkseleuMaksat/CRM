package kz.akseleu.crm.service;

import kz.akseleu.crm.model.Application;
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
                    SELECT a.id, a.user_name, a.course_id, a.commentary, a.phone, a.handled,
                           c.name AS course_name
                    FROM t_application a
                    JOIN t_course c ON c.id = a.course_id
                    ORDER BY a.id DESC
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet st = statement.executeQuery()) {

            List<Application> applications = new ArrayList<>();

            while (st.next()) {
                Application a = new Application();
                a.setId(st.getLong("id"));
                a.setUser_name(st.getString("user_name"));
                a.setCourse_id(st.getInt("course_id"));
                a.setCourse_name(st.getString("course_name"));
                a.setCommentary(st.getString("commentary"));
                a.setPhone(st.getString("phone"));
                a.setHandled(st.getBoolean("handled"));
                applications.add(a);
            }
            return applications;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Application getApplicationById(Long id) {
        String sql = """
                    SELECT a.id, a.user_name, a.course_id, a.commentary, a.phone, a.handled,
                           c.name AS course_name
                    FROM t_application a
                    JOIN t_course c ON c.id = a.course_id
                    WHERE a.id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet st = statement.executeQuery()) {
                if (!st.next()) return null;

                Application a = new Application();
                a.setId(st.getLong("id"));
                a.setUser_name(st.getString("user_name"));
                a.setCourse_id(st.getInt("course_id"));
                a.setCourse_name(st.getString("course_name"));
                a.setCommentary(st.getString("commentary"));
                a.setPhone(st.getString("phone"));
                a.setHandled(st.getBoolean("handled"));
                return a;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean createApplication(Application application) {
        String sql = """
                    INSERT INTO t_application (user_name, course_id, commentary, phone, handled)
                    VALUES (?,?,?,?,?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, application.getUser_name());
            statement.setInt(2, application.getCourse_id());
            statement.setString(3, application.getCommentary());
            statement.setString(4, application.getPhone());
            statement.setBoolean(5, application.isHandled());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateApplication(Application application) {
        String sql = """
                    UPDATE t_application SET
                    user_name = ?, course_id = ?, commentary = ?, phone = ?, handled = ?
                    WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, application.getUser_name());
            statement.setInt(2, application.getCourse_id());
            statement.setString(3, application.getCommentary());
            statement.setString(4, application.getPhone());
            statement.setBoolean(5, application.isHandled());
            statement.setLong(6, application.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteApplication(Long id) {
        String sql = "DELETE FROM t_application WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
