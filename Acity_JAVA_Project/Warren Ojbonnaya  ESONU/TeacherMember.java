public class TeacherMember extends Member {
    public TeacherMember(String name, int id) {
        super(name, id);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new LibraryManagementGUI2().createAndShowGUI());
    }
}