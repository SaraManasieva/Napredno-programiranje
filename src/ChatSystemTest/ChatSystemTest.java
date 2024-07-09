package ChatSystemTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;

class NoSuchRoomException extends Exception
{

    public NoSuchRoomException(String message) {
        super(message);
    }
}

class NoSuchUserException extends Exception
{
    public NoSuchUserException(String message) {
        super(message);
    }
}

class ChatRoom
{
    private String imeNaSoba;
    private Set<String> setOdIminjaNaKorisniciVoSoba;

    ChatRoom(String name)
    {
        this.imeNaSoba=name;
        this.setOdIminjaNaKorisniciVoSoba=new TreeSet<>();
    }

    void addUser(String username)
    {
        this.setOdIminjaNaKorisniciVoSoba.add(username);
    }

    void removeUser(String username)
    {
        this.setOdIminjaNaKorisniciVoSoba.remove(username);
    }

    boolean hasUser(String username)
    {
        return this.setOdIminjaNaKorisniciVoSoba.contains(username);
    }

    int numUsers()
    {
        return this.setOdIminjaNaKorisniciVoSoba.size();
    }
    public String getImeNaSoba() {
        return imeNaSoba;
    }

    public void setImeNaSoba(String imeNaSoba) {
        this.imeNaSoba = imeNaSoba;
    }

    public Set<String> getSetOdIminjaNaKorisniciVoSoba() {
        return setOdIminjaNaKorisniciVoSoba;
    }

    public void setSetOdIminjaNaKorisniciVoSoba(Set<String> setOdIminjaNaKorisniciVoSoba) {
        this.setOdIminjaNaKorisniciVoSoba = setOdIminjaNaKorisniciVoSoba;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        //room1
        //user1
        //user2
        sb.append(getImeNaSoba()).append("\n");
        if(getSetOdIminjaNaKorisniciVoSoba().isEmpty())
        {
            sb.append("EMPTY").append("\n");
        }
        else
        {
            getSetOdIminjaNaKorisniciVoSoba().stream().forEach(str->sb.append(str).append("\n"));
        }
        return sb.toString();
    }
}

class ChatSystem
{
    private Map<String,ChatRoom> mapaOdChatRoomPoImeNaChatRoom;
    private Set<String> setOdIminjaNaKorisniciNAChatSystem;

    public ChatSystem() {
        this.mapaOdChatRoomPoImeNaChatRoom=new TreeMap<>();
        this.setOdIminjaNaKorisniciNAChatSystem=new TreeSet<>();
    }

    void addRoom(String roomName)
    {
        this.mapaOdChatRoomPoImeNaChatRoom.put(roomName,new ChatRoom(roomName));
    }

    void removeRoom(String roomName)
    {
        this.mapaOdChatRoomPoImeNaChatRoom.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if(mapaOdChatRoomPoImeNaChatRoom.containsKey(roomName))
        {
            return this.mapaOdChatRoomPoImeNaChatRoom.get(roomName);
        }
        throw new NoSuchRoomException(roomName);
    }

    void register(String userName)
    {
        this.setOdIminjaNaKorisniciNAChatSystem.add(userName);
        Optional<ChatRoom> minChatRoom=this.mapaOdChatRoomPoImeNaChatRoom.values().stream()
                .min(Comparator.comparing(ChatRoom::numUsers)).stream().findFirst();
        if(minChatRoom.isPresent())
        {
            minChatRoom.get().addUser(userName);
        }
        else
        {
            return;
        }
    }

    void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if(!mapaOdChatRoomPoImeNaChatRoom.containsKey(roomName))
        {
            throw new NoSuchRoomException(roomName);
        }
        if(!this.setOdIminjaNaKorisniciNAChatSystem.contains(userName))
        {
            throw new NoSuchUserException(userName);
        }
        ChatRoom cr=this.mapaOdChatRoomPoImeNaChatRoom.get(roomName);
        cr.addUser(userName);
    }

    void registerAndJoin(String userName, String roomName)
    {
        this.setOdIminjaNaKorisniciNAChatSystem.add(userName);
        this.mapaOdChatRoomPoImeNaChatRoom.get(roomName).addUser(userName);
    }

    void leaveRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (mapaOdChatRoomPoImeNaChatRoom.containsKey(roomName)) {
            if(mapaOdChatRoomPoImeNaChatRoom.get(roomName).getSetOdIminjaNaKorisniciVoSoba().contains(username))
            {
                mapaOdChatRoomPoImeNaChatRoom.get(roomName).getSetOdIminjaNaKorisniciVoSoba().remove(username);
            }
            else
            {
                throw new NoSuchUserException(username);
            }
        }
        else
        {
            throw new NoSuchRoomException(roomName);
        }
    }

    void followFriend(String username, String friend_username) throws NoSuchUserException {
        if(!this.setOdIminjaNaKorisniciNAChatSystem.contains(friend_username))
        {
            throw new NoSuchUserException(username);
        }
        this.mapaOdChatRoomPoImeNaChatRoom.values().stream().filter(cr->cr.hasUser(friend_username))
                .forEach(cr->cr.addUser(username));

    }

}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs,params);
                    }
                }
            }
        }
    }

}
