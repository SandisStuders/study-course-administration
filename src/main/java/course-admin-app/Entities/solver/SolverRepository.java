package Avocado.main_project.Entities.solver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolverRepository extends JpaRepository<Solver, SolverKey> {
}
