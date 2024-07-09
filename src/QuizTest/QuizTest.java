package QuizTest;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

class InvalidOperationException extends Exception
{
    public InvalidOperationException(String message) {
        super(message);
    }
}
abstract class Question
{
    protected String text;
    protected int points;

    public Question(String text, int points) {
        this.text = text;
        this.points = points;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    abstract double getPoeniOdPrasanje(String dadenOdgovor);
}

class TocnoNetocnoPrasanje extends Question
{
    private boolean answer;

    public TocnoNetocnoPrasanje(String text, int points,boolean answer) {
        super(text, points);
        this.answer=answer;
    }

    @Override
    public String toString() {
        return String.format("True/False Question: %s Points: %d Answer: %s",text,points,answer);
    }

    @Override
    double getPoeniOdPrasanje(String dadenOdgovor) {
        return this.answer==Boolean.parseBoolean(dadenOdgovor)?points:0;
    }
}

class PovekeIzbornoPrasanje extends Question
{
    private String answer;

    public PovekeIzbornoPrasanje(String text, int points,String answer) throws InvalidOperationException {
        super(text, points);
        List<String> lista=List.of("A","B","C","D","E");
//        for (int i=0;i<lista.size();i++)
//        {
            //if(!(answer.equals(lista.get(i))))
            if(answer.charAt(0)<'A'||answer.charAt(0)>'E')
            {
                throw new InvalidOperationException(String.format("%s is not allowed option for this question",answer));
            }
        //}
        this.answer=answer;

    }

    @Override
    public String toString() {
        return String.format("Multiple Choice Question: %s Points %d Answer: %s",text,points,answer);
    }

    @Override
    double getPoeniOdPrasanje(String dadenOdgovor) {
        return dadenOdgovor.equals(answer)?points:(-0.2)*points;
    }
}
class Quiz
{
    private List<Question> listaOdPrasanja;

    public Quiz() {
        this.listaOdPrasanja=new ArrayList<>();
    }

    void addQuestion(String questionData)  {
        //TF;text;points;answer
        String[]parts=questionData.split(";");
        if(parts[0].equals("TF"))
        {
            listaOdPrasanja.add(new TocnoNetocnoPrasanje(parts[1],Integer.parseInt(parts[2]),Boolean.parseBoolean(parts[3])));
        }
        else
        {
            try {
                listaOdPrasanja.add(new PovekeIzbornoPrasanje(parts[1],Integer.parseInt(parts[2]),parts[3]));
            } catch (InvalidOperationException e) {
                System.out.println(String.format("%s is not allowed option for this question",parts[3]));
            }
        }
    }


    void printQuiz(OutputStream os)
    {
        PrintWriter pw=new PrintWriter(os);
        this.listaOdPrasanja.stream().sorted(Comparator.comparing(Question::getPoints).reversed())
                        .forEach(pr->pw.println(pr));
        pw.flush();
    }

    void answerQuiz (List<String> answers, OutputStream os) {
        //1. -0.60
        //2. -0.80
        //3. 2.00
        PrintWriter pw=new PrintWriter(os);
        AtomicInteger a=new AtomicInteger(1);
        if(answers.size()!=this.listaOdPrasanja.size())
        {
            try {
                throw new InvalidOperationException("Answers and questions must be of same length!");
            } catch (InvalidOperationException e) {
                pw.println(e.getMessage());
            }
        }
        else
        {
            double totalPoints=IntStream.range(0,answers.size())
                    .mapToDouble(broj->{
                        pw.println(String.format("%d. %.2f",a.getAndIncrement(),listaOdPrasanja.get(broj).getPoeniOdPrasanje(answers.get(broj))));
                        return listaOdPrasanja.get(broj).getPoeniOdPrasanje(answers.get(broj));
                    }).sum();
            pw.println(String.format("Total points: %.2f",totalPoints));
        }
        pw.flush();

    }
}

public class QuizTest {
    public static void main(String[] args)  {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i=0;i<questions;i++) {
            quiz.addQuestion(sc.nextLine());
        }

        List<String> answers = new ArrayList<>();

        int answersCount =  Integer.parseInt(sc.nextLine());

        for (int i=0;i<answersCount;i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase==1) {
            quiz.printQuiz(System.out);
        } else if (testCase==2) {
            quiz.answerQuiz(answers, System.out);
        } else {
            System.out.println("Invalid test case");
        }
    }
}
