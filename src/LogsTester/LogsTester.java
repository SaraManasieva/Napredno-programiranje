package LogsTester;

import java.util.*;
import java.util.stream.Collectors;

abstract class Log
{
    //service_name microservice_name type message timestamp
    protected String service_name;
    protected String microservice_name;
    protected String type;
    protected String message;
    protected long timestamp;

    public Log(String service_name, String microservice_name, String type, String message, long timestamp) {
        this.service_name = service_name;
        this.microservice_name = microservice_name;
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
    }
    abstract int getSerioznost();

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getMicroservice_name() {
        return microservice_name;
    }

    public void setMicroservice_name(String microservice_name) {
        this.microservice_name = microservice_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


}

class InfoLog extends Log
{

    public InfoLog(String service_name, String microservice_name, String type, String message, long timestamp) {
        super(service_name, microservice_name, type, message, timestamp);
    }

    @Override
    int getSerioznost() {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("%s|%s [%s] %s T:%d",getService_name(),
               getMicroservice_name(),getType(),getMessage() ,getTimestamp());
    }
}
class Warnlog extends Log
{

    public Warnlog(String service_name, String microservice_name, String type, String message, long timestamp) {
        super(service_name, microservice_name, type, message, timestamp);
    }

    @Override
    int getSerioznost() {
        int s=1;
        if(getMessage().toLowerCase().contains("might cause error"))
        {
            s++;
        }
        return s;
    }

    @Override
    public String toString() {
        //service1|microservice3 [WARN] Log message 4. 437 T:437
        return String.format("%s|%s [%s] %s T:%d",getService_name(),
                getMicroservice_name(),getType(),getMessage() ,getTimestamp());
    }
}
class ErrorLog extends Log
{

    public ErrorLog(String service_name, String microservice_name, String type, String message, long timestamp) {
        super(service_name, microservice_name, type, message, timestamp);
    }

    @Override
    int getSerioznost() {
        int s=3;
        if(getMessage().toLowerCase().contains("fatal"))
        {
            s=s+2;
        }
        if(getMessage().toLowerCase().contains("exception"))
        {
            s=s+3;
        }
        return s;
    }

    @Override
    public String toString() {
        //service1|microservice3 [WARN] Log message 4. 437 T:437
        return String.format("%s|%s [%s] %s T:%d",getService_name(),
                getMicroservice_name(),getType(),getMessage() ,getTimestamp());
    }
}

class MikroServis
{
    private String imeNaMikroServis;
    private List<Log> listaOdLogovi=new ArrayList<>();

    public MikroServis(String imeNaMikroServis) {
        this.imeNaMikroServis = imeNaMikroServis;
        //this.listaOdLogovi=new ArrayList<>();
    }

    void addLogVoMikroservis (String log)
    {
        //service_name microservice_name type message timestamp
        String[]parts=log.split("\\s+");
        String message=Arrays.stream(parts).skip(3).limit(parts.length-3)
                .collect(Collectors.joining(" "));
        if(parts[2].equalsIgnoreCase("INFO"))
        {
            this.listaOdLogovi.add(new InfoLog(parts[0],parts[1],parts[2],message,Long.parseLong(parts[parts.length-1])));
        }
        if(parts[2].equalsIgnoreCase("ERROR"))
        {
            this.listaOdLogovi.add(new ErrorLog(parts[0],parts[1],parts[2],message,Long.parseLong(parts[parts.length-1])));
        }
        if(parts[2].equalsIgnoreCase("WARN"))
        {
            this.listaOdLogovi.add(new Warnlog(parts[0],parts[1],parts[2],message,Long.parseLong(parts[parts.length-1])));
        }

    }
    public double getProsecnaSerioznostNaMikroServis()
    {
        return getListaOdLogovi().stream().mapToDouble(log->log.getSerioznost()).average().getAsDouble();
    }

    public String getImeNaMikroServis() {
        return imeNaMikroServis;
    }

    public void setImeNaMikroServis(String imeNaMikroServis) {
        this.imeNaMikroServis = imeNaMikroServis;
    }

    public List<Log> getListaOdLogovi() {
        return listaOdLogovi;
    }

    public void setListaOdLogovi(List<Log> listaOdLogovi) {
        this.listaOdLogovi = listaOdLogovi;
    }

    @Override
    public String toString() {
        return "";
        //StringBuilder sb=new StringBuilder();
        //sb.append(String.format("displayLogs service2 microservice1 NEWEST_FIRST",))
    }
}

class Servis
{
    private String imeNaServis;
    private Map<String,MikroServis> mapaOdMikroServisiPoImeNaMikroServis=new TreeMap<String,MikroServis>();

    public Servis(String imeNaServis) {
        this.imeNaServis = imeNaServis;
        //this.mapaOdMikroServisiPoImeNaMikroServis=new HashMap<>();
    }

    public void displayLogsVoServis(String ms, String order)
    {
        Comparator<Log>comparator=Comparator.comparing(Log::getSerioznost).thenComparing(Log::getTimestamp);
        if(order.equals("NEWEST_FIRST"))
        {
            comparator=Comparator.comparing(Log::getTimestamp).reversed();


        }
        if(order.equals("OLDEST_FIRST"))
        {
            comparator=Comparator.comparing(Log::getTimestamp);


        }
        if(order.equals("MOST_SEVERE_FIRST"))
        {
            comparator=Comparator.comparing(Log::getSerioznost).thenComparing(Log::getTimestamp).reversed();


        }
        if(order.equals("LEAST_SEVERE_FIRST"))
        {
            comparator=Comparator.comparing(Log::getSerioznost).thenComparing(Log::getTimestamp);
        }
        List<Log>listLog;
        if(ms==null)
        {
            listLog=mapaOdMikroServisiPoImeNaMikroServis.values().stream().flatMap(m->m.getListaOdLogovi().stream())
                    .collect(Collectors.toList());
        }
        else
        {
            listLog=mapaOdMikroServisiPoImeNaMikroServis.get(ms).getListaOdLogovi();
        }
        listLog.stream().sorted(comparator).forEach(l-> System.out.println(l));
    }
    void addLogVoServis (String log)
    {
        //service_name microservice_name type message timestamp
        String[]parts=log.split("\\s+");
        mapaOdMikroServisiPoImeNaMikroServis.putIfAbsent(parts[1],new MikroServis(parts[1]));
        mapaOdMikroServisiPoImeNaMikroServis.get(parts[1]).addLogVoMikroservis(log);
    }

    public double getProsecnaSerioznostNaServis()
    {
        return this.mapaOdMikroServisiPoImeNaMikroServis.values().stream().flatMap(ms->ms.getListaOdLogovi().stream())
                .mapToInt(log->log.getSerioznost()).average().orElse(0);
    }
    public String getImeNaServis() {
        return imeNaServis;
    }

    public void setImeNaServis(String imeNaServis) {
        this.imeNaServis = imeNaServis;
    }

    public Map<String, MikroServis> getMapaOdMikroServisiPoImeNaMikroServis() {
        return mapaOdMikroServisiPoImeNaMikroServis;
    }

    public void setMapaOdMikroServisiPoImeNaMikroServis(Map<String, MikroServis> mapaOdMikroServisiPoImeNaMikroServis) {
        this.mapaOdMikroServisiPoImeNaMikroServis = mapaOdMikroServisiPoImeNaMikroServis;
    }

    @Override
    public String toString() {
        IntSummaryStatistics is=mapaOdMikroServisiPoImeNaMikroServis
                .values().stream().flatMap(mikroServis -> mikroServis.getListaOdLogovi().stream())
                .mapToInt(Log::getSerioznost).summaryStatistics();
        return String.format("Service name: %s Count of microservices: %d Total logs in service: %d Average severity for all logs: %.2f Average number of logs per microservice: %.2f",
                getImeNaServis(),mapaOdMikroServisiPoImeNaMikroServis.size(),
                is.getCount(),is.getAverage(),is.getCount()/(float) mapaOdMikroServisiPoImeNaMikroServis.size());
    }
}
class LogCollector
{
    private Map<String,Servis> mapaOdServisiPoImeNaServis=new HashMap<>();

    public LogCollector() {
//        this.mapaOdServisiPoImeNaServis=new HashMap<>();
    }

    void addLog (String log)
    {
        String[]parts=log.split("\\s+");
        //service_name microservice_name type message timestamp
        this.mapaOdServisiPoImeNaServis.putIfAbsent(parts[0],new Servis(parts[0]));
        this.mapaOdServisiPoImeNaServis.get(parts[0]).addLogVoServis(log);
    }

    void printServicesBySeverity()
    {
        //метод кој ќе ги испечати сите сервиси за кои колекторот има собрано логови сортирани според просечната
        // сериозност (анг. severity) на сите логовите произведени од тој сервис во опаѓачки редослед.
        this.mapaOdServisiPoImeNaServis.values().stream().sorted(Comparator.comparing(Servis::getProsecnaSerioznostNaServis).reversed())
                .forEach(log-> System.out.println(log));
    }



    Map<Integer, Integer> getSeverityDistribution (String service, String microservice)  {

        // метод кој враќа мапа од нивоата на сериозност детектирани во логовите на микросервисот microservice кој се наоѓа во сервисот service
        // со бројот на логови кои ја имаат соодветната сериозност. Доколку microservice e null, потребно е резултатот да се однесува на сите логови
        // од конкретниот сервис (без разлика на микросервисот).
        //if(mapaOdServisiPoImeNaServis.get(service).getMapaOdMikroServisiPoImeNaMikroServis().get(microservice)==null)
        if(microservice==null)
        {
            return this.mapaOdServisiPoImeNaServis.get(service).getMapaOdMikroServisiPoImeNaMikroServis().values()
                    .stream().flatMap(ms->ms.getListaOdLogovi().stream())
                    .collect(Collectors.groupingBy(log->log.getSerioznost(),TreeMap::new,Collectors.summingInt(log->1)));
        }
        else
        {
            return this.mapaOdServisiPoImeNaServis.get(service).getMapaOdMikroServisiPoImeNaMikroServis().get(microservice)
                    .getListaOdLogovi().stream().collect(Collectors.groupingBy(log->log.getSerioznost(),TreeMap::new,Collectors.summingInt(log->1)));
        }

    }

    void displayLogs(String service, String microservice, String order) {
        // метод кој ги печати логовите на микросервисот microservice кој се наоѓа во сервисот service сортирани
        // според правилото дадено во order. Правилото може да ги има следните 4 вредности:
        mapaOdServisiPoImeNaServis.get(service).displayLogsVoServis(microservice,order);
//        Comparator<Log>comparator=Comparator.comparing(Log::getSerioznost);
//        if(order.equals("NEWEST_FIRST"))
//        {
//            comparator=Comparator.comparing(Log::getTimestamp).reversed();
//            if(microservice==null)
//            System.out.println(String.format("displayLogs %s NEWEST_FIRST",service));
//            else
//            System.out.println(String.format("displayLogs %s %s NEWEST_FIRST",service,microservice));
//
//        }
//        if(order.equals("OLDEST_FIRST"))
//        {
//            comparator=Comparator.comparing(Log::getTimestamp);
//            if(microservice==null)
//            System.out.println(String.format("displayLogs %s OLDEST_FIRST",service));
//            else
//                System.out.println(String.format("displayLogs %s %s OLDEST_FIRST",service,microservice));
//
//        }
//        if(order.equals("MOST_SEVERE_FIRST"))
//        {
//            comparator=Comparator.comparing(Log::getSerioznost).reversed();
//            if(microservice==null)
//            System.out.println(String.format("displayLogs %s MOST_SEVERE_FIRST",service));
//            else
//                System.out.println(String.format("displayLogs %s %s MOST_SEVERE_FIRST",service,microservice));
//
//        }
//        if(order.equals("LEAST_SEVERE_FIRST"))
//        {
//            comparator=Comparator.comparing(Log::getSerioznost);
//            if(microservice==null)
//            System.out.println(String.format("displayLogs %s LEAST_SEVERE_FIRST",service));
//            else
//                System.out.println(String.format("displayLogs %s %s LEAST_SEVERE_FIRST",service,microservice));
//
//        }
//        List<Log>listaOdLogovi;
//        if(microservice==null)
//        {
//            this.mapaOdServisiPoImeNaServis.get(service).getMapaOdMikroServisiPoImeNaMikroServis().values()
//                    .stream().flatMap(ms->ms.getListaOdLogovi().stream())
//                    .sorted(comparator).forEach(log -> System.out.println(log));
//        }
//        else
//        {
//            this.mapaOdServisiPoImeNaServis.get(service).getMapaOdMikroServisiPoImeNaMikroServis().get(microservice)
//                    .getListaOdLogovi().stream().sorted(comparator).forEach(log -> System.out.println(log));
//        }
//        //System.out.println(mapaOdServisiPoImeNaServis.get(service).getMapaOdMikroServisiPoImeNaMikroServis().get(microservice));
////        if(mapaOdServisiPoImeNaServis.get(service).getMapaOdMikroServisiPoImeNaMikroServis().get(microservice)==null)
////        {
////            System.out.println("Greska");
////        }
////        else
//
//
//
////            this.mapaOdServisiPoImeNaServis.get(service).getMapaOdMikroServisiPoImeNaMikroServis().get(microservice)
////                    .getListaOdLogovi().stream().sorted(comparator).forEach(log-> {
////                        System.out.println(log);
////                    });


    }

    public Map<String, Servis> getMapaOdServisiPoImeNaServis() {
        return mapaOdServisiPoImeNaServis;
    }

    public void setMapaOdServisiPoImeNaServis(Map<String, Servis> mapaOdServisiPoImeNaServis) {
        this.mapaOdServisiPoImeNaServis = mapaOdServisiPoImeNaServis;
    }

    @Override
    public String toString() {
        return "LogCollector{" +
                "mapaOdServisiPoImeNaServis=" + mapaOdServisiPoImeNaServis +
                '}';
    }
}

public class LogsTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LogCollector collector = new LogCollector();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("addLog")) {
                collector.addLog(line.replace("addLog ", ""));
            } else if (line.startsWith("printServicesBySeverity")) {
                collector.printServicesBySeverity();
            } else if (line.startsWith("getSeverityDistribution")) {
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                if (parts.length == 3) {
                    microservice = parts[2];
                }
                collector.getSeverityDistribution(service, microservice).forEach((k,v)-> System.out.printf("%d -> %d%n", k,v));
            } else if (line.startsWith("displayLogs")){
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                String order = null;
                if (parts.length == 4) {
                    microservice = parts[2];
                    order = parts[3];
                } else {
                    order = parts[2];
                }
                System.out.println(line);
                collector.displayLogs(service, microservice, order);
            }
        }
    }
}