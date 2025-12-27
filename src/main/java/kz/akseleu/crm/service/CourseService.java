package kz.akseleu.crm.service;

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
public class CourseService {
    private final Connection connection;

    public List<Course> getAllCourses() {
        String sql = "SELECT id, name, description, price FROM t_course ORDER BY name";
        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            List<Course> list = new ArrayList<>();
            while (rs.next()) {
                Course c = new Course();
                c.setId(rs.getLong("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                c.setPrice(rs.getInt("price"));
                list.add(c);
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Course getById(Long id) {
        String sql = "SELECT id, name, description, price FROM t_course WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setLong(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (!rs.next()) return null;

                Course c = new Course();
                c.setId(rs.getLong("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                c.setPrice(rs.getInt("price"));
                return c;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}