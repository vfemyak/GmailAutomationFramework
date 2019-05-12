package models;

public class Letter {
    private String to;
    private String subject;
    private String message;

    public Letter(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    public Letter() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean checkMessageFields(Letter letter) {
        return this.to.equals(letter.getTo())
                && this.subject.equals(letter.getSubject())
                && this.message.equals(letter.getMessage());
    }

}
