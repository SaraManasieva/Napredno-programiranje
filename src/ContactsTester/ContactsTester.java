package ContactsTester;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

abstract class Contact
{
    protected String date;
    Contact(String date)
    {
        this.date=date;
    }
    boolean isNewerThan(Contact c)
    {
        return this.date.compareTo(c.date)>0;
    }
    abstract String quotet();

    abstract String getType();

    public String getDate() {
        return date;
    }
}
class EmailContact extends Contact
{
    private String email;
    EmailContact(String date, String email)
    {
        super(date);
        this.email=email;
    }
    String getEmail()
    {
        return email;
    }

    @Override
    String quotet() {
        return String.format("\"%s\"",email);
    }

    @Override
    String getType() {
        return "Email";
    }
}
enum Operator { VIP, ONE, TMOBILE }
class PhoneContact extends Contact
{
    private String broj;
    private Operator operator;
    PhoneContact(String date, String phone)
    {
        super(date);
        this.broj=phone;
        String str=phone.substring(0,3);
        if(str.equals("070")||str.equals("071")||str.equals("072"))
            operator=Operator.TMOBILE;
        else if (str.equals("075")||str.equals("076"))
            operator=Operator.ONE;
        else if(str.equals("078")||str.equals("077"))
            operator=Operator.VIP;
        else
            operator=null;
    }
    String getPhone()
    {
        return broj;
    }
    Operator getOperator()
    {
       return this.operator;
    }


    @Override
    String quotet() {
        return String.format("\"%s\"",broj);
    }

    @Override
    String getType() {
        return "Phone";
    }
}
class Student
{
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    private Contact[]nizaOdKontakti;
    Student(String firstName, String lastName, String city, int age, long index)
    {
        this.firstName=firstName;
        this.lastName=lastName;
        this.city=city;
        this.age=age;
        this.index=index;
        this.nizaOdKontakti=new Contact[0];
    }
    void addEmailContact(String date, String email)
    {
        this.nizaOdKontakti=Arrays.copyOf(nizaOdKontakti,nizaOdKontakti.length+1);
        this.nizaOdKontakti[nizaOdKontakti.length-1]=new EmailContact(date,email);
    }
    void addPhoneContact(String date, String phone)
    {
        this.nizaOdKontakti=Arrays.copyOf(nizaOdKontakti,nizaOdKontakti.length+1);
        this.nizaOdKontakti[this.nizaOdKontakti.length-1]=new PhoneContact(date,phone);
    }
    public int getContactsSize()
    {
        return nizaOdKontakti.length;
    }

    Contact[] getEmailContacts()
    {
        return Arrays.stream(nizaOdKontakti).filter(k->k.getType().equals("Email")).toArray((int value) -> new Contact[value]);
    }
    Contact[]getPhoneContacts()
    {
        return Arrays.stream(nizaOdKontakti).filter(k->k.getType().equals("Phone")).toArray((int value) -> new Contact[value]);
    }

    String getCity()
    {
        return city;
    }
    String getFullName()
    {
        return String.format("%s %s",firstName,lastName);
    }
    long getIndex()
    {
        return index;
    }
    Contact getLatestContact()
    {
        return Arrays.stream(nizaOdKontakti).sorted(Comparator.comparing(Contact::getDate).reversed()).findFirst().orElse(null);
    }
    //private String firstName;
    //    private String lastName;
    //    private String city;
    //    private int age;
    //    private long index;
    public static String  keyValue(String key,String value)
    {
        return String.format("\"%s\":\"%s\"",key,value);
    }
    public static String keyValueBezNavodniciValue(String key,String value)
    {
        return String.format("\"%s\":%s",key,value);
    }

    @Override
    public String toString() {
        //{"ime":"Mile", "prezime":"Jovanov", "vozrast":24, "grad":"Skopje", "indeks":81974965, "telefonskiKontakti"
        // :["078/186-236", "077/524-486", "077/848-344", "077/428-158", "075/905-387", "071/160-774"], "emailKontakti"
        // :["eikyqppxexavtua@gmail.com", "rpzdnz@hotmail.com", "qtyagx@finki.ukim.mk"]}
        //
        //{"ime":"Jovan", "prezime":"Jovanov", "vozrast":20, "grad":"Skopje", "indeks":101010,
        // "telefonskiKontakti":["077/777-777", "078/888-888"], "emailKontakti":["jovan.jovanov@example.com",
        // "jovanov@jovan.com", "jovan@jovanov.com"]}
        StringJoiner joiner=new StringJoiner(", ","{","}");
        joiner.add(keyValue("ime",firstName));
        joiner.add(keyValue("prezime",lastName));
        joiner.add(keyValueBezNavodniciValue("vozrast",String.valueOf(age)));
        joiner.add(keyValue("grad",city));
        joiner.add(keyValueBezNavodniciValue("indeks",String.valueOf(index)));
        String contactsString=Arrays.stream(getPhoneContacts()).map(k->k.quotet()).collect(Collectors.joining(", ","[","]"));
        joiner.add(keyValueBezNavodniciValue("telefonskiKontakti",contactsString));
        String contactsEmail=Arrays.stream(getEmailContacts()).map(k->k.quotet()).collect(Collectors.joining(", ","[","]"));
        joiner.add(keyValueBezNavodniciValue("emailKontakti",contactsEmail));
        return joiner.toString();
    }
}

class Faculty
{
    private String name;
    private Student[]students;
    Faculty(String name, Student [] students)
    {
        this.name=name;
        this.students=Arrays.copyOf(students,students.length);
    }

    int countStudentsFromCity(String cityName)
    {
        return(int)Arrays.stream(students).filter(s->s.getCity().equals(cityName)).count();
    }

    Student getStudent(long index)
    {
        return Arrays.stream(students).filter(s->s.getIndex()==index).findFirst().orElse(null);
    }
    double getAverageNumberOfContacts()
    {
        return Arrays.stream(students).mapToInt(s->s.getContactsSize()).average().orElse(0.0);
    }

    Student getStudentWithMostContacts()
    {
        return Arrays.stream(students).sorted(Comparator.comparing(Student::getContactsSize).thenComparing(Student::getIndex).reversed())
                .findFirst().orElse(null);
    }

    @Override
    public String toString() {
        //{"fakultet":"FINKI", "studenti":[STUDENT1, STUDENT2, ...]}
        StringJoiner joiner=new StringJoiner(", ","{","}");
        joiner.add(Student.keyValue("fakultet",name));
        String studentti=Arrays.stream(students).map(s->s.toString()).collect(Collectors.joining(", ","[","]"));
        joiner.add(Student.keyValueBezNavodniciValue("studenti",studentti));
        return joiner.toString();
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
