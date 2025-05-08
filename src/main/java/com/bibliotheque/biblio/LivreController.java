package com.bibliotheque.biblio;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LivreController {

    // Liste de livres en mémoire
    private List<Livre> livres = new ArrayList<>();

    // Constructeur : initialisation des livres
    public LivreController() {
        livres.add(new Livre("Les Misérables", "Victor Hugo", true));
        livres.add(new Livre("1984", "George Orwell", false));
        livres.add(new Livre("Le Petit Prince", "Antoine de Saint-Exupéry", true));
        livres.add(new Livre("L'Alchimiste", "Paulo Coelho", true));
    }

    // Méthode pour afficher la page d'accueil avec une recherche
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("livres", livres);
        return "index";
    }

    // Méthode de recherche des livres
    @GetMapping("/rechercher")
    public String rechercher(@RequestParam(name = "query", required = false) String query, Model model) {
        List<Livre> resultatRecherche = new ArrayList<>();

        if (query != null && !query.isEmpty()) {
            for (Livre livre : livres) {
                if (livre.getTitre().toLowerCase().contains(query.toLowerCase())) {
                    resultatRecherche.add(livre);
                }
            }
        }

        model.addAttribute("resultatRecherche", resultatRecherche.isEmpty() ? "Aucun résultat trouvé." : resultatRecherche);
        return "index";
    }
}