package test;

import java.util.ArrayList;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentMain {

	public static Scanner sc = new Scanner(System.in);
	public static final int INPUT = 1, PRINT = 2, ANLYZE = 3, SEARCH = 4, UPDATE = 5, SORT = 6, DELETE = 7, EXIT = 8;

	public static void main(String[] args) {
		// ������������
		boolean run = true;
		int no = 0;
		ArrayList<Student> list = new ArrayList<>();
		DBConnection dbCon = new DBConnection();
		// ���ѷ�Ʈ
		while (run) {
			System.out.println("============================================================");
			System.out.println("1.�Է� | 2.��� | 3.�м� | 4.�˻� | 5.���� | 6.���� | 7.���� | 8.����");
			System.out.println("============================================================");
			System.out.print(">>");
			no = Integer.parseInt(sc.nextLine());
			switch (no) {
			case INPUT:
				// �����ڸ� ���� �ؾߵ�. �̸�,����,����,����,����
				Student student = inputDataStudent();
				// �����ͺ��̽� �Է�
				int rValue = dbCon.insert(student);
				if (rValue == 1) {
					System.out.println("���� ����");
				} else {
					System.out.println("���� ����");
				}
				break;
			case PRINT:
				ArrayList<Student> list2 = dbCon.select();
				if (list == null) {
					System.out.println("select ����");
				} else {
					printStudent(list2);
				}
				break;
			case ANLYZE:
				// �м�: �̸�, ����, ����, ���, ���
				ArrayList<Student> list3 = dbCon.analyzeSelect();
				printAnalyze(list3);
				break;
			case SEARCH:
				// �л��˻� �̸��ޱ�
				String dataName = searchStudent();
				ArrayList<Student> list4 = dbCon.nameSearchSelect(dataName);
				if (list4.size() >= 1) {
					System.out.println(list4);
				} else {
					System.out.println("�л� �˻� ��� �����Դϴ�.");
				}
				break;
			case UPDATE:
				// �л��˻� ������ �����ؼ� ����
				int updateReturnValue = 0;
				int id = inputId();
				Student stu = dbCon.selectId(id);
				if (stu == null) {
					System.out.println("���� ���� �߻� ");
				} else {
					Student updateStudent = updateStudent(stu);
					updateReturnValue = dbCon.update(updateStudent);
				}
				if (updateReturnValue == 1) {
					System.out.println("update �Ϸ�");
				} else {
					System.out.println("update ����");
				}
				break;
			case SORT:
				ArrayList<Student> list5 = dbCon.selectSort();
				if (list5 == null) {
					System.out.println("���� ����");
				} else {
					printStudent(list5);
				}
//				Collections.sort(list, Collections.reverseOrder());
//				sortStudent(list);
				break;
			case DELETE:
				// �й�(id) �˻�
				int deleteId = inputId();
				int deleteReturnValue = dbCon.delete(deleteId);
				if (deleteReturnValue == 1) {
					System.out.println("���� ����");
				} else {
					System.out.println("���� ����");
				}
				break;
			case EXIT:
				run = false;
				break;
			}
		} // end of while

		System.out.println("The End");
	}// end of main
		// id �Է�

	private static int inputId() {
		boolean run = true;
		int id = 0;
		System.out.print("Id �Է�(����) : ");
		while (run) {
			try {
				id = Integer.parseInt(sc.nextLine());
				if (id > 0 && id < Integer.MAX_VALUE) {
					run = false;
				}
			} catch (Exception e) {
				System.out.println("id �Է� ���� : " + e.getMessage());
			}

		}
		return id;
	}
	// �л� ���� ����

	private static Student updateStudent(Student stu) {
		int kor = inputScoreSubject(stu.getName(), stu.getKor(), "����");
		stu.setKor(kor);
		int eng = inputScoreSubject(stu.getName(), stu.getEng(), "����");
		stu.setEng(eng);
		int math = inputScoreSubject(stu.getName(), stu.getMath(), "����");
		stu.setMath(math);

		stu.calTotal();
		stu.calAvg();
		stu.calGrade();
		System.out.println(stu);
		return stu;
	}
	// ���� ���� ����

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
					System.out.println("�Է°��� ������ ������ϴ�.");
				}
			} catch (Exception e) {
				System.out.println("���� �Է¿� ���� ���");
				data = 0;
			}
		}
		return data;
	}
	// �̸� �Է� ���� �˻�

	private static String matchingNamePattern() {
		String name = null;
		while (true) {

			try {
				System.out.print("�˻��� �л��̸�: ");
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[��-�R]{2,4}$");
				Matcher matcher = pattern.matcher(name);
				if (!matcher.find()) {
					System.out.println("�̸� �Է¿� ������ �߻��߽��ϴ�. �ٽ����Է¿�û�մϴ�.");
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("�Է¿��� ������ �߻��߽��ϴ�.");
				break;
			}
		}
		return name;
	}
	// �л� �˻�

	private static String searchStudent() {
		String name = null;
		boolean flag = true;
		name = matchingNamePattern();
		while (flag) {
			System.out.print("�˻��� �л��̸�: ");

			Pattern pattern = Pattern.compile("^[��-�R]{2,4}$");
			Matcher matcher = pattern.matcher(name);
			if (!matcher.find()) {
				System.out.println("�̸��Է¿����߻� �ٽ����Է¿�û�մϴ�.");
			} else {
				flag = false;
				break;
			}
		}
		return name;
	}
	// �м�

	private static void printAnalyze(ArrayList<Student> list) {
		for (Student data : list) {
			System.out.println(data.getId() + "\t" + data.getName() + "\t" + data.getAge() + "\t" + data.getTotal()
					+ "\t" + String.format("%.2f", data.getAvg()) + "\t" + data.getGrade());
		}
	}
	// �л� ���� ���

	private static void printStudent(ArrayList<Student> list) {
		System.out.println("��ȣ \t �̸� \t����\t����\t����\t����\t����\t���\t���");
		for (Student data : list) {
			System.out.println(data);
		}
	}
	// �л� ���� �Է�

	private static Student inputDataStudent() {
		String name = inputName();
		int age = inputAge();
		int kor = inputScore("����");
		int eng = inputScore("����");
		int math = inputScore("����");
		Student student = new Student(name, age, kor, eng, math);
		student.calTotal();
		student.calAvg();
		student.calGrade();
		return student;
	}
	// �̸� �Է�

	private static String inputName() {
		String name = null;
		while (true) {
			try {
				System.out.println("�̸� :");
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[��-�R]{2,4}$");
				Matcher matcher = pattern.matcher(String.valueOf(name));
				if (matcher.find()) {
					break;
				} else {
					System.out.println("�̸��� �߸��Է��Ͽ����ϴ�. ���Է¿�û");
				}
			} catch (NumberFormatException e) {
				System.out.println("�Է¿����߻��߽��ϴ�.");
				name = null;
				break;
			}

		}
		return name;
	}
	// ���� ���� �Է�

	private static int inputScore(String string) {
		int score = 0;
		while (true) {
			try {
				System.out.println(string + "���� :");
				score = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(score));
				if (matcher.find() && score <= 100) {
					break;
				} else {
					System.out.println("������ �߸��Է��Ͽ����ϴ�. ���Է¿�û");
				}

			} catch (NumberFormatException e) {
				System.out.println("�Է¿����߻��߽��ϴ�.");
				score = 0;
				break;
			}

		}
		return score;
	}

	// ���� �Է�
	private static int inputAge() {
		int age = 0;
		while (true) {
			try {
				System.out.println("���� : ");
				age = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(age));
				if (matcher.find() && age <= 100) {
					break;
				} else {
					System.out.println("���̸� �߸��Է��Ͽ����ϴ�. ���Է¿�û");
				}

			} catch (NumberFormatException e) {
				System.out.println("�����Է� ���� �߻�");
				age = 0;
				break;
			}
		}
		return age;
	}
}
