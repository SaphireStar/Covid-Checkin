package au.usyd.elec5619.service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.algorithms.Algorithm;

/**
 * Token verification and generation
 * @author Yiqing Yang yyan8151
 *
 */
@Service(value="tokenManager")
public class TokenManager{
	// secret
	private static final String SECRET = "ADshqzfxaZZqsFXuSuNb2230Bpqc";
	
	// salt
	private static final String SALT = "E2MDQ1NTUyMTgsInV";
	
	/*
	 * Generates a token
	 */
	public String generateToken(String userId) {
		String token = "";
		
		try {
            //expire date
            Calendar currentTime = Calendar.getInstance();
            currentTime.add(Calendar.HOUR, 24);
            Date expireDate = currentTime.getTime();
            
            // header
            Map<String,Object> header = new HashMap<String, Object>();
            header.put("typ","JWT");
            header.put("alg","HS256");
            
            // token
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("username"+SALT,userId)
                    .withExpiresAt(expireDate)
                    .sign(Algorithm.HMAC256(SECRET));
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
        return token;
	}

	/** 
	 * Verifies token
	 */
	public boolean verifyToken(String token) {
		try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch (Exception e){
            return  false;
        }
	}
}
