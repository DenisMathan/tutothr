package tutothr.auth.verifikation;

import java.util.Optional;

import tutothr.common.MyBaseRepository;

// Typ des IDs ist jetzt String
public interface VerificationRepositoryI extends MyBaseRepository<VerificationToken, String> {
    // findByToken ist nicht mehr nötig, da findById(token) das Gleiche tut
    @Override
    public Optional<VerificationToken> findById(String token);
    public Optional<VerificationToken> findByUserId(Long userId);
}
