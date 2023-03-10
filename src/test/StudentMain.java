package test;

import java.util.ArrayList;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentMain {

	public static Scanner sc = new Scanner(System.in);
	public static final int INPUT = 1, PRINT = 2, ANLYZE = 3, SEARCH = 4, UPDATE = 5, SORT = 6, DELETE = 7, EXIT = 8;

	public static void main(String[] args) {
		// Áö¿ªº¯¼ö¼±¾ğ
		boolean run = true;
		int no = 0;
		ArrayList<Student> list = new ArrayList<>();
		DBConnection dbCon = new DBConnection();
		// ¹«ÇÑ·çÆ®
		while (run) {
			System.out.println("============================================================");
			System.out.println("1.ÀÔ·Â | 2.Ãâ·Â | 3.ºĞ¼® | 4.°Ë»ö | 5.¼öÁ¤ | 6.Á¤·Ä | 7.»èÁ¦ | 8.Á¾·á");
			System.out.println("============================================================");
			System.out.print(">>");
			no = Integer.parseInt(sc.nextLine());
			switch (no) {
			case INPUT:
				// »ı¼ºÀÚ¸¦ ¼±ÅÃ ÇØ¾ßµÊ. ÀÌ¸§,³ªÀÌ,±¹¾î,¿µ¾î,¼öÇĞ
				Student student = inputDataStudent();
				// µ¥ÀÌÅÍº£ÀÌ½º ÀÔ·Â
				int rValue = dbCon.insert(student);
				if (rValue == 1) {
					System.out.println("»ğÀÔ ¼º°ø");
				} else {
					System.out.println("»ğÀÔ ½ÇÆĞ");
				}
				break;
			case PRINT:
				ArrayList<Student> list2 = dbCon.select();
				if (list == null) {
					System.out.println("select ½ÇÆĞ");
				} else {
					printStudent(list2);
				}
				break;
			case ANLYZE:
				// ºĞ¼®: ÀÌ¸§, ³ªÀÌ, ÃÑÁ¡, Æò±Õ, µî±Ş
				ArrayList<Student> list3 = dbCon.analyzeSelect();
				printAnalyze(list3);
				break;
			case SEARCH:
				// ÇĞ»ı°Ë»ö ÀÌ¸§¹Ş±â
				String dataName = searchStudent();
				ArrayList<Student> list4 = dbCon.nameSearchSelect(dataName);
				if (list4.size() >= 1) {
					System.out.println(list4);
				} else {
					System.out.println("ÇĞ»ı °Ë»ö °á°ú ¿À·ùÀÔ´Ï´Ù.");
				}
				break;
			case UPDATE:
				// ÇĞ»ı°Ë»ö Á¡¼ö¸¦ ¼öÁ¤ÇØ¼­ ÀúÀå
				int updateReturnValue = 0;
				int id = inputId();
				Student stu = dbCon.selectId(id);
				if (stu == null) {
					System.out.println("¼öÁ¤ ¿À·ù ¹ß»ı ");
				} else {
					Student updateStudent = updateStudent(stu);
					updateReturnValue = dbCon.update(updateStudent);
				}
				if (updateReturnValue == 1) {
					System.out.println("update ¿Ï·á");
				} else {
					System.out.println("update ½ÇÆĞ");
				}
				break;
			case SORT:
				ArrayList<Student> list5 = dbCon.selectSort();
				if (list5 == null) {
					System.out.println("Á¤·Ä ½ÇÆĞ");
				} else {
					printStudent(list5);
				}
//				Collections.sort(list, Collections.reverseOrder());
//				sortStudent(list);
				break;
			case DELETE:
				// ÇĞ¹ø(id) °Ë»ö
				int deleteId = inputId();
				int deleteReturnValue = dbCon.delete(deleteId);
				if (deleteReturnValue == 1) {
					System.out.println("»èÁ¦ ¼º°ø");
				} else {
					System.out.println("»èÁ¦ ½ÇÆĞ");
				}
				break;
			case EXIT:
				run = false;
				break;
			}
		} // end of while

		System.out.println("The End");
	}// end of main
		// id ÀÔ·Â

	private static int inputId() {
		boolean run = true;
		int id = 0;
		System.out.print("Id ÀÔ·Â(¼ıÀÚ) : ");
		while (run) {
			try {
				id = Integer.parseInt(sc.nextLine());
				if (id > 0 && id < Integer.MAX_VALUE) {
					run = false;
				}
			} catch (Exception e) {
				System.out.println("id ÀÔ·Â ¿À·ù : " + e.getMessage());
			}

		}
		return id;
	}
	// ÇĞ»ı Á¤º¸ ¼öÁ¤

	private static Student updateStudent(Student stu) {
		int kor = inputScoreSubject(stu.getName(), stu.getKor(), "±¹¾î");
		stu.setKor(kor);
		int eng = inputScoreSubject(stu.getName(), stu.getEng(), "¿µ¾î");
		stu.setEng(eng);
		int math = inputScoreSubject(stu.getName(), stu.getMath(), "¼öÇĞ");
		stu.setMath(math);

		stu.calTotal();
		stu.calAvg();
		stu.calGrade();
		System.out.println(stu);
		return stu;
	}
	// °ú¸ñº° ¼ºÀû ¼öÁ¤

	private static int inputScoreSubject(String name, int score, String subject) {
		boolean run = true;
		int data = 0;
		while (run) {
			System.out.print(name + " " + subject + " " + score + ">>");
			try {
				data = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(data));
				if (matcher.find() && data > 0 && data <= 100) {
					run = false;
				} else {
					System.out.println("ÀÔ·Â°ªÀÇ ¹üÀ§¸¦ ¹ş¾î³µ½À´Ï´Ù.");
				}
			} catch (Exception e) {
				System.out.println("Á¡¼ö ÀÔ·Â¿¡ ¿À·ù ¹æ»ı");
				data = 0;
			}
		}
		return data;
	}
	// ÀÌ¸§ ÀÔ·Â ÆĞÅÏ °Ë»ö

	private static String matchingNamePattern() {
		String name = null;
		while (true) {

			try {
				System.out.print("°Ë»öÇÒ ÇĞ»ıÀÌ¸§: ");
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[°¡-ÆR]{2,4}$");
				Matcher matcher = pattern.matcher(name);
				if (!matcher.find()) {
					System.out.println("ÀÌ¸§ ÀÔ·Â¿¡ ¿À·ù°¡ ¹ß»ıÇß½À´Ï´Ù. ´Ù½ÃÀçÀÔ·Â¿äÃ»ÇÕ´Ï´Ù.");
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("ÀÔ·Â¿¡¼­ ¿À·ù°¡ ¹ß»ıÇß½À´Ï´Ù.");
				break;
			}
		}
		return name;
	}
	// ÇĞ»ı °Ë»ö

	private static String searchStudent() {
		String name = null;
		boolean flag = true;
		name = matchingNamePattern();
		while (flag) {
			System.out.print("°Ë»öÇÑ ÇĞ»ıÀÌ¸§: ");

			Pattern pattern = Pattern.compile("^[°¡-ÆR]{2,4}$");
			Matcher matcher = pattern.matcher(name);
			if (!matcher.find()) {
				System.out.println("ÀÌ¸§ÀÔ·Â¿À·ù¹ß»ı ´Ù½ÃÀçÀÔ·Â¿äÃ»ÇÕ´Ï´Ù.");
			} else {
				flag = false;
				break;
			}
		}
		return name;
	}
	// ºĞ¼®

	private static void printAnalyze(ArrayList<Student> list) {
		for (Student data : list) {
			System.out.println(data.getId() + "\t" + data.getName() + "\t" + data.getAge() + "\t" + data.getTotal()
					+ "\t" + String.format("%.2f", data.getAvg()) + "\t" + data.getGrade());
		}
	}
	// ÇĞ»ı Á¤º¸ Ãâ·Â

	private static void printStudent(ArrayList<Student> list) {
		System.out.println("¹øÈ£ \t ÀÌ¸§ \t³ªÀÌ\t±¹¾î\t¿µ¾î\t¼öÇĞ\tÃÑÁ¡\tÆò±Õ\tµî±Ş");
		for (Student data : list) {
			System.out.println(data);
		}
	}
	// ÇĞ»ı Á¤º¸ ÀÔ·Â

	private static Student inputDataStudent() {
		String name = inputName();
		int age = inputAge();
		int kor = inputScore("±¹¾î");
		int eng = inputScore("¿µ¾î");
		int math = inputScore("¼öÇĞ");
		Student student = new Student(name, age, kor, eng, math);
		student.calTotal();
		student.calAvg();
		student.calGrade();
		return student;
	}
	// ÀÌ¸§ ÀÔ·Â

	private static String inputName() {
		String name = null;
		while (true) {
			try {
				System.out.println("ÀÌ¸§ :");
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[°¡-ÆR]{2,4}$");
				Matcher matcher = pattern.matcher(String.valueOf(name));
				if (matcher.find()) {
					break;
				} else {
					System.out.println("ÀÌ¸§¸¦ Àß¸øÀÔ·ÂÇÏ¿´½À´Ï´Ù. ÀçÀÔ·Â¿äÃ»");
				}
			} catch (NumberFormatException e) {
				System.out.println("ÀÔ·Â¿À·ù¹ß»ıÇß½À´Ï´Ù.");
				name = null;
				break;
			}

		}
		return name;
	}
	// °ú¸ñº° Á¡¼ö ÀÔ·Â

	private static int inputScore(String string) {
		int score = 0;
		while (true) {
			try {
				System.out.println(string + "Á¡¼ö :");
				score = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(score));
				if (matcher.find() && score <= 100) {
					break;
				} else {
					System.out.println("Á¡¼ö¸¦ Àß¸øÀÔ·ÂÇÏ¿´½À´Ï´Ù. ÀçÀÔ·Â¿äÃ»");
				}

			} catch (NumberFormatException e) {
				System.out.println("ÀÔ·Â¿À·ù¹ß»ıÇß½À´Ï´Ù.");
				score = 0;
				break;
			}

		}
		return score;
	}

	// ³ªÀÌ ÀÔ·Â
	private static int inputAge() {
		int age = 0;
		while (true) {
			try {
				System.out.println("³ªÀÌ : ");
				age = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(age));
				if (matcher.find() && age <= 100) {
					break;
				} else {
					System.out.println("³ªÀÌ¸¦ Àß¸øÀÔ·ÂÇÏ¿´½À´Ï´Ù. ÀçÀÔ·Â¿äÃ»");
				}

			} catch (NumberFormatException e) {
				System.out.println("³ªÀÌÀÔ·Â ¿À·ù ¹ß»ı");
				age = 0;
				break;
			}
		}
		return age;
	}
}
