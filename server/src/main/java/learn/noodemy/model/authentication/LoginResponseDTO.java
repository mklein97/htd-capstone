package learn.noodemy.model.authentication;

public class LoginResponseDTO {
    private String jwtToken;
    private String roleName;
    private String username;
    private int userId;

    public LoginResponseDTO() {}
    public LoginResponseDTO(String jwtToken, String username, int userId, String roleName) {
        this.jwtToken = jwtToken;
        this.roleName = roleName;
        this.username = username;
        this.userId = userId;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
