import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String Message;
    private String recievedFrom;
    private String groupName;
    private String numOfUsers;

    public MessageModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getRecievedFrom() {
        return recievedFrom;
    }

    public void setRecievedFrom(String recievedFrom) {
        this.recievedFrom = recievedFrom;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getNumOfUsers() {
        return numOfUsers;
    }

    public void setNumOfUsers(String numOfUsers) {
        this.numOfUsers = numOfUsers;
    }
}
