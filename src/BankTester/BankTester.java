package BankTester;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface Parser
{
    public static double parseStringToDouble(String amountSo$)
    {
        return Double.parseDouble(amountSo$.substring(0,amountSo$.length()-1));
    }
}
class Account
{
    private String name;
    private long id;
    private String balance;

    Account(String name, String balance)
    {
        this.balance =balance;
        this.name =name;
        Random random=new Random();
        this.id=random.nextLong();
    }

    String getBalance()
    {
        return balance;
    }

    String getName()
    {
        return name;
    }

    long getId()
    {
        return id;
    }

    void setBalance(String balance)
    {
        this.balance=balance;
    }

    @Override
    public String toString() {
        //Name:Andrej Gajduk\n
        //Balance:20.00$\n
        return String.format("Name: %s\nBalance: %s\n",name,balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && name.equals(account.name) && balance.equals(account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, balance);
    }
}

abstract class Transaction
{
    private final long fromId;
    private final long toId;
    private final String description;
    private final String amount;

    public Transaction(long fromId, long toId, String description, String amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = amount;
    }
    abstract double getProvizija();

    String getAmount()
    {
        return amount;
    }

    long getFromId()
    {
        return fromId;
    }

    long getToId()
    {
        return toId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return fromId == that.fromId && toId == that.toId && description.equals(that.description) && amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId, toId, description, amount);
    }

    public String getDescription() {
        return description;
    }
}

class FlatAmountProvisionTransaction extends Transaction
{
    private String flatprovision;
    // public Transaction(long fromId, long toId, String description, String amount) {
    FlatAmountProvisionTransaction(long fromId, long toId,String amount, String flatProvision)
    {
        super(fromId,toId,"FlatAmount",amount);
        this.flatprovision=flatProvision;
    }

    @Override
    double getProvizija() {
        return Parser.parseStringToDouble(getFlatAmount());
    }

    String getFlatAmount()
    {
        return flatprovision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatAmountProvisionTransaction that = (FlatAmountProvisionTransaction) o;
        return flatprovision.equals(that.flatprovision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flatprovision);
    }
}

class FlatPercentProvisionTransaction extends Transaction
{
    private int centsPerDolar;
    // public Transaction(long fromId, long toId, String description, String amount) {
    FlatPercentProvisionTransaction (long fromId, long toId, String amount, int centsPerDolar)
    {
        super(fromId,toId,"FlatPercent",amount);
        this.centsPerDolar=centsPerDolar;
    }

    int getPercent()
    {
        return centsPerDolar;
    }

    @Override
    double getProvizija() {
        //return (int)Parser.parseStringToDouble(getAmount())*centsPerDolar/100;
        return (int)Parser.parseStringToDouble(getAmount())*centsPerDolar/100.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatPercentProvisionTransaction that = (FlatPercentProvisionTransaction) o;
        return centsPerDolar == that.centsPerDolar;
    }

    @Override
    public int hashCode() {
        return Objects.hash(centsPerDolar);
    }
}

class Bank
{
    private Account accounts[];
    private String name;
    private double totalTransfers;
    private double totalProvision;

    Bank(String name, Account accounts[])
    {
        this.name=name;
        this.accounts=Arrays.copyOf(accounts,accounts.length);
        this.totalProvision=0;
        this.totalTransfers=0;
    }

    boolean makeTransaction(Transaction t)
    {
        int indexFrom= IntStream.range(0,accounts.length)
                .filter(i->t.getFromId()==accounts[i].getId()).findFirst().orElse(-1);
        int indexTo= IntStream.range(0,accounts.length)
                .filter(i->t.getToId()==accounts[i].getId()).findFirst().orElse(-1);
        double balanceFromId=Parser.parseStringToDouble(accounts[indexFrom].getBalance());
        double balanceTpId=Parser.parseStringToDouble(accounts[indexTo].getBalance());
        double tAmount=Parser.parseStringToDouble(t.getAmount());
        if(indexFrom==-1||indexTo==-1)
        {
            return false;
        }
        if(balanceFromId<tAmount)
        {
            return false;
        }
        double provizija=t.getProvizija();
        totalTransfers=totalTransfers+tAmount;
        totalProvision=totalProvision+provizija;
        balanceFromId=balanceFromId-tAmount-provizija;
        if(indexFrom==indexTo)
        {
            balanceTpId=balanceTpId-provizija;
        }
        else
        {
            balanceTpId=balanceTpId+tAmount;
        }
        accounts[indexFrom].setBalance(String.format("%.2f$",balanceFromId));
        accounts[indexTo].setBalance(String.format("%.2f$",balanceTpId));
        return true;
    }

    String totalTransfers()
    {
        return String.format("%.2f$",totalTransfers);
    }

    String totalProvision()
    {
        return String.format("%.2f$",totalProvision);
    }

    @Override
    public String toString() {
        //Name:Banka na RM\n
        //\n
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("Name: %s",name)).append("\n").append("\n");
        Arrays.stream(accounts)
                .forEach(ac->sb.append(ac));
        return sb.toString();
    }

    public Account[] getAccounts() {
        return accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Double.compare(bank.totalTransfers, totalTransfers) == 0 && Double.compare(bank.totalProvision, totalProvision) == 0 && Arrays.equals(accounts, bank.accounts) && name.equals(bank.name);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, totalTransfers, totalProvision);
        result = 31 * result + Arrays.hashCode(accounts);
        return result;
    }
}




public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        //accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}
