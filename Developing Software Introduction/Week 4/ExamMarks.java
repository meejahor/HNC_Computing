import java.util.Scanner;

public class ExamMarks {

    private static String FindGrade(int mark) {
        if (mark >= 70) {
            return "A";
        } else if (mark >= 60) {
            return "B";
        } else if (mark >= 50) {
            return "C";
        } else if (mark >= 40) {
            return "D";
        }

        return "F";
    }

    private static void ShowGrade(String message, String grade) {
        System.out.println(message + grade);
    }

    public static void main(String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        int examMark1, examMark2, examMarkTotal;

        System.out.print("Please enter exam mark 1: ");
        examMark1 = keyboard.nextInt();
        System.out.print("Please enter exam mark 2: ");
        examMark2 = keyboard.nextInt();

        examMarkTotal = examMark1 + examMark2;
        examMarkTotal /= 2;

        ShowGrade("Grade for exam 1: ", FindGrade(examMark1));
        ShowGrade("Grade for exam 2: ", FindGrade(examMark2));
        ShowGrade("Average grade: ", FindGrade(examMarkTotal));
    }
}
