package br.com.api.habitFlow.repository;


import br.com.api.habitFlow.model.habito.Frequency;
import br.com.api.habitFlow.model.habito.Habito;
import br.com.api.habitFlow.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitoRepository extends JpaRepository<Habito ,Long> {
    @Query("SELECT h FROM Habito h WHERE " +
            "(:frequency IS NULL OR h.frequency = :frequency) AND" +
            "(:active IS NULL OR h.active = :active) AND" + "(h.user = :user)" )
    List<Habito> encontrarPersonalizado(Frequency frequency, Boolean active, User user);

    @Query("SELECT h FROM Habito h WHERE h.id = :id AND h.user = :user")
    Optional<Habito> listarHabito(Long id, User user);
}
