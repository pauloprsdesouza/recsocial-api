package br.com.api.infrastructure.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

    @Value("${security.jwt.expiration}")
    private String _expiration;

    @Value("${security.jwt.signature.key}")
    private String _signatureKey;

    public String generateToken(UserAccount userAccount) {
        long expiration = Long.valueOf(_expiration);
        LocalDateTime dateHourExpiration = LocalDateTime.now().plusMinutes(expiration);

        Date date = Date.from(dateHourExpiration.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder().setSubject(userAccount.getEmail()).setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, _signatureKey).compact();
    }

    private Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts.parser().setSigningKey(_signatureKey).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            Date expirationDate = claims.getExpiration();

            LocalDateTime date =
                    expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            return !LocalDateTime.now().isAfter(date);
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) throws ExpiredJwtException {
        return (String) getClaims(token).getSubject();
    }

}
