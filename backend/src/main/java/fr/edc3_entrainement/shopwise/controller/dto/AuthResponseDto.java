package fr.edc3_entrainement.shopwise.controller.dto;

public class AuthResponseDto {
    private String token;
    public AuthResponseDto() {}
    public AuthResponseDto(String token) { this.token = token; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
