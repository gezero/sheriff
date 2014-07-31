package sheriff.core.services;

import sheriff.core.entities.Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Jiri on 7. 7. 2014.
 */
@Repository
public interface KeysRepository extends JpaRepository<Key, Long>, KeysRepositoryCustom {
    Key findByPublicKey(String publicKey);
}
