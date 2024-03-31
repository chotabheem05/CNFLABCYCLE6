package Lab6;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class StudentMain {
    private static final SessionFactory sessionFactory;
    
    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void insert(int id, String usn, String name, String address, int totalMarks) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Student student = new Student();
            student.setId(id);
            student.setUsn(usn);
            student.setName(name);
            student.setAddress(address);
            student.setTotalMarks(totalMarks);
            session.save(student);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Student student = session.get(Student.class, id);
            if (student != null) {
                session.delete(student);
                System.out.println(id + " Deleted Successfully");
            } else {
                System.out.println(id + " not found");
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void display() {
        try (Session session = sessionFactory.openSession()) {
            List<Student> students = session.createQuery("from Student", Student.class).list();
            System.out.println("List of Students");
            for (Student student : students) {
                System.out.println(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void search(int id) {
        try (Session session = sessionFactory.openSession()) {
            Student student = session.get(Student.class, id);
            if (student != null) {
                System.out.println("Student Details");
                System.out.println(student);
            } else {
                System.out.println("Student with ID " + id + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        StudentMain sm = new StudentMain();
        Scanner scanner = new Scanner(System.in);

        lp: while (true) {
            System.out.println("1: Insert");
            System.out.println("2: Delete");
            System.out.println("3: Search");
            System.out.println("4: Display");
            System.out.println("7: Exit");
            System.out.println("Make Your Choice");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter the Student Details to insert");
                    System.out.print("Enter the Student id: ");
                    int id = scanner.nextInt();
                    System.out.print("Enter the Student usn: ");
                    String usn = scanner.next();
                    System.out.print("Enter the Student name: ");
                    String name = scanner.next();
                    System.out.print("Enter the Student address: ");
                    String address = scanner.next();
                    System.out.print("Enter the Student totalMarks: ");
                    int totalMarks = scanner.nextInt();
                    sm.insert(id, usn, name, address, totalMarks);
                    break;

                case 2:
                    System.out.print("Enter student id to delete: ");
                    id = scanner.nextInt();
                    sm.delete(id);
                    break;

                case 3:
                    System.out.print("Enter student id to search: ");
                    id = scanner.nextInt();
                    sm.search(id);
                    break;

                case 4:
                    sm.display();
                    break;

                case 7:
                    break lp;

                default:
                    System.out.println("Invalid Choice");
            }
        }
        // Close the scanner when done to avoid resource leakage
        scanner.close();
        // Close the session factory when the application shuts down
        sessionFactory.close();
    }
}