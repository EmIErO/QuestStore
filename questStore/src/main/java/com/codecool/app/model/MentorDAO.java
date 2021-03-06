// package com.codecool.app.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MentorDAO {
    Connection connection;
    CommonDAO commonDAO;


    public MentorDAO(Connection connection, CommonDAO commonDAO){
        this.connection = connection;
        this.commonDAO = commonDAO;
    }

    public String checkIfIdInTable(int id){
        String findAllIds = "SELECT id_systemuser FROM mentor;";
        List<Integer> allIds = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement(findAllIds);
            ResultSet result = commonDAO.getData(connection, ps);

            while (result.next()) {
                allIds.add(result.getInt("id_systemuser"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (allIds.contains(id)) {
            return "mentor";
            
        } else {
            return "";
        }
    }


    public int getIdMentor(int userId){
        String findByUserId = "SELECT id_mentor FROM mentor WHERE id_systemUser=?;";
        int idMentor = Integer.MAX_VALUE;

        try {
            PreparedStatement ps = connection.prepareStatement(findByUserId);
            ps.setInt(1, userId);
            ResultSet result = commonDAO.getData(connection, ps);

            while (result.next()) {
                idMentor = result.getInt("id_mentor");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idMentor;
    }


    public List<Classroom> getMentorClassrooms(int idMentor){
        List<Classroom> mentorClassrooms = new ArrayList<>();
        String findClassByMentor = "SELECT * FROM mentor_class JOIN class_ ON class_.id_class = mentor_class.id_class WHERE id_mentor = ?;";
        String classroomName;
        int idClassroom;
        
        try {
            PreparedStatement ps = connection.prepareStatement(findClassByMentor);
            ps.setInt(1, idMentor);
            ResultSet result = commonDAO.getData(connection, ps);

            while (result.next()) {
                classroomName = result.getString("name");
                idClassroom = result.getInt("id_class");
                Classroom classroom = new Classroom(classroomName,idClassroom);
                mentorClassrooms.add(classroom);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mentorClassrooms;
        
    }


    public void setMentorClassroom(List<Integer> idsClassroom, int idMentor){
        String setClasses = "INSERT INTO mentor_class (id_mentor, id_class) VALUES (?,?);";
        try {
            for (Integer idClassroom : idsClassroom) {
                PreparedStatement ps = connection.prepareStatement(setClasses);
                ps.setInt(1, idMentor);
                ps.setInt(2, idClassroom);
                commonDAO.updateData(connection, ps);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

}