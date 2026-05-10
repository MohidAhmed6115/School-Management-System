package com.school.controller.dashboards.admin;

import com.school.controller.dashboards.admin.announcements.AddAnnouncementController;
import com.school.controller.dashboards.admin.announcements.RemoveAnnouncementController;
import com.school.controller.dashboards.student.StudentDashboardController;
import com.school.controller.dashboards.teacher.TeacherDashboardController;
import com.school.model.Student;
import com.school.model.announcements.Announcements;
import com.school.model.attendance.StudentAttendance;
import com.util.SchoolDataManager;
import com.util.SchoolDataStore;
import com.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.util.Tools.animateStripe;

public class AdminDashboardController extends AdminController {
    @FXML public VBox manageTeacherButton;
    @FXML public VBox manageStudentButton;
    @FXML public VBox libraryButton;
    @FXML public VBox teacherAnnouncementBox;
    @FXML public VBox studentAnnouncementBox;
    @FXML public HBox studentAnnouncementActions;
    @FXML public HBox teacherAnnouncementActions;
    @FXML private Label totalStudentsLabel;
    @FXML private Label totalTeachersLabel;
    @FXML private Label todayAttendance;
    @FXML private Label totalBooksLabel;
    @FXML private Region heroStripe;

    @FXML
    protected void initialize() {
        animateStripe(heroStripe);

        SchoolDataStore.teacherAnnouncements  = SchoolDataManager.loadTeacherAnnouncements();
        SchoolDataStore.studentAnnouncements  = SchoolDataManager.loadStudentAnnouncements();

        usernameLabel.setText(SchoolDataStore.currentUser.getName());
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));

        totalStudentsLabel.setText(String.valueOf(SchoolDataStore.students.size()));
        totalTeachersLabel.setText(String.valueOf(SchoolDataStore.teachers.size()));

        // getting total books
        totalBooksLabel.setText(String.valueOf(com.library.controller.librarian.LibrarianFunctions.totalBooks()));

        // calculating total attendance
        int totalAttendance = 0;
        int totalPresent = 0;
        for (Student st : SchoolDataStore.students) {

            StudentAttendance attendance = SchoolDataStore.getStudentAttendance(st.getSapId(), st.getCurrentSemester());

            if (attendance == null) {
                continue;
            }
            boolean isPresent = attendance.isPresent();

            totalAttendance++;
            if (isPresent) {
                totalPresent++;
            }
        }
        int attendancePercentage = (totalPresent*100)/totalAttendance;
        todayAttendance.setText(attendancePercentage + "%");

        // After everything else in initialize()
        studentAnnouncementBox.prefWidthProperty().bind(
                studentAnnouncementBox.getParent().layoutBoundsProperty().map(b -> b.getWidth() / 2 - 6)
        );
        teacherAnnouncementBox.prefWidthProperty().bind(
                teacherAnnouncementBox.getParent().layoutBoundsProperty().map(b -> b.getWidth() / 2 - 6)
        );

        // shows announcements
        showAnnouncements();
    }

    @FXML
    private void handleManageTeachers() throws IOException {
        SceneManager.loadScene(logoutButton, "/school/fxml/dashboards/admin/manageTeachers/manage-teachers.fxml");
    }

    @FXML
    private void handleManageStudents() throws IOException {
        SceneManager.loadScene(logoutButton, "/school/fxml/dashboards/admin/manageStudents/manage-students.fxml");
    }

    @FXML
    private void handleLibrary() throws IOException {
        SceneManager.loadScene(logoutButton, "/library/fxml/librarian-page.fxml");
    }

    public void showAnnouncements() {
        ArrayList<Announcements> studentAnnouncements = new ArrayList<>();
        studentAnnouncements = SchoolDataStore.studentAnnouncements;

        ArrayList<Announcements> teacherAnnouncements = new ArrayList<>();
        teacherAnnouncements = SchoolDataStore.teacherAnnouncements;

        for (Announcements announcement : studentAnnouncements) {
            addStudentAnnouncements(announcement.getMessage());
        }

        for (Announcements announcement : teacherAnnouncements) {
            addTeacherAnnouncements(announcement.getMessage());
        }
    }

    public void addStudentAnnouncements(String announcement) {
        Label row = new Label("• " + announcement);
        row.getStyleClass().add("notice-item");

        row.setWrapText(true);
        row.setMaxWidth(Double.MAX_VALUE);

        // insert before the last child (the actions HBox)
        int insertIndex = studentAnnouncementBox.getChildren().indexOf(studentAnnouncementActions);
        studentAnnouncementBox.getChildren().add(insertIndex, row);
    }

    public void addTeacherAnnouncements(String announcement) {
        Label row = new Label("• " + announcement);
        row.getStyleClass().add("notice-item");

        row.setWrapText(true);
        row.setMaxWidth(Double.MAX_VALUE);

        // insert before the last child (the actions HBox)
        int insertIndex = teacherAnnouncementBox.getChildren().indexOf(teacherAnnouncementActions);
        teacherAnnouncementBox.getChildren().add(insertIndex, row);
    }

    @FXML
    public void addStudentAnnouncement() {
        SceneManager.openPopup(logoutButton, "/school/fxml/dashboards/admin/announcements/add-announcement.fxml",
                (AddAnnouncementController c) -> c.setType("student"));
        refreshAnnouncements();
    }

    @FXML
    public void addTeacherAnnouncement() {
        SceneManager.openPopup(logoutButton, "/school/fxml/dashboards/admin/announcements/add-announcement.fxml",
                (AddAnnouncementController c) -> c.setType("teacher"));
        refreshAnnouncements();
    }

    @FXML
    public void removeStudentAnnouncement() {
        SceneManager.openPopup(logoutButton, "/school/fxml/dashboards/admin/announcements/remove-announcement.fxml",
                (RemoveAnnouncementController c) -> c.setType("student"));
        refreshAnnouncements();
    }

    @FXML
    public void removeTeacherAnnouncement() {
        SceneManager.openPopup(logoutButton, "/school/fxml/dashboards/admin/announcements/remove-announcement.fxml",
                (RemoveAnnouncementController c) -> c.setType("teacher"));
        refreshAnnouncements();
    }

    private void refreshAnnouncements() {
        studentAnnouncementBox.getChildren().removeIf(
                node -> node.getStyleClass().contains("notice-item")
        );
        teacherAnnouncementBox.getChildren().removeIf(
                node -> node.getStyleClass().contains("notice-item")
        );

        showAnnouncements();
    }
}
