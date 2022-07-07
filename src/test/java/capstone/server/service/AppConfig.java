package capstone.server.service;


import capstone.server.repository.challenge.ChallengeRepositoryCustom;
import capstone.server.repository.challenge.ChallengeRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
public class AppConfig {
    @PersistenceContext
    private EntityManager em;
    @Bean
    public ChallengeRepositoryCustom challengeRepository(){
        return new ChallengeRepositoryImpl(new JPAQueryFactory(em));
    }



}
