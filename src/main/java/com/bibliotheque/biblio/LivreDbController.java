package com.bibliotheque.biblio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LivreDbController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Page d'accueil : liste des livres depuis la base
    @GetMapping("/db")
    public String homeDb(Model model) {
        List<Livre> livres = jdbcTemplate.query(
            "SELECT titre, auteur, disponible FROM livres",
            (rs, rowNum) -> new Livre(
                rs.getString("titre"),
                rs.getString("auteur"),
                rs.getBoolean("disponible")
            )
        );
        model.addAttribute("livres", livres);
        return "index";  // réutilise ta vue Thymeleaf existante
    }

    // Recherche : /db/rechercher?query=...
    @GetMapping("/db/rechercher")
    public String rechercherDb(@RequestParam(name = "query", required = false) String query,
                               Model model) {
        List<Livre> resultatRecherche;
        if (query != null && !query.isEmpty()) {
            resultatRecherche = jdbcTemplate.query(
                "SELECT titre, auteur, disponible FROM livres WHERE LOWER(titre) LIKE ?",
                new Object[]{"%" + query.toLowerCase() + "%"},
                (rs, rowNum) -> new Livre(
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getBoolean("disponible")
                )
            );
        } else {
            // même requête que homeDb
            resultatRecherche = jdbcTemplate.query(
                "SELECT titre, auteur, disponible FROM livres",
                (rs, rowNum) -> new Livre(
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getBoolean("disponible")
                )
            );
        }
        model.addAttribute("resultatRecherche",
            resultatRecherche.isEmpty() ? "Aucun résultat trouvé." : resultatRecherche);
        return "index";
    }
}