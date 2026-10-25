// =============================================================================
// Fichier     : src/test/java/fr/oc/devops/backend/controllers/
//               ApiContractTest.java
// Rôle        : tests de CONTRAT des 5 routes de l'API : vraie requête HTTP
//               (MockMvc) sur les contrôleurs, JSON comparé EXACTEMENT. Écrits
//               avant la migration vers Spring Boot 4 pour prouver que l'API
//               reste identique pour ses utilisateurs (ticket #1).
// Utilise     : NotionController, WorkshopController, WebMvcConfig (préfixe
//               /api), services simulés par Mockito (pas de base de données)
// Utilisé par : ./gradlew test (donc scripts/run-tests.sh et la CI)
// =============================================================================
package fr.oc.devops.backend.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.oc.devops.backend.models.Notion;
import fr.oc.devops.backend.models.Workshop;
import fr.oc.devops.backend.services.NotionService;
import fr.oc.devops.backend.services.WorkshopService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;

// Ne démarre que la couche web (contrôleurs, JSON, validation), sans base
@WebMvcTest(controllers = {NotionController.class, WorkshopController.class})
class ApiContractTest {

    @Autowired
    private MockMvc mvc;

    // Services remplacés par des doublures : on teste le contrat HTTP seul
    @MockitoBean
    private NotionService notionService;

    @MockitoBean
    private WorkshopService workshopService;

    // Fabrique une notion (objet généré depuis la spécification OpenAPI)
    private static Notion notion(String name) {
        Notion n = new Notion();
        n.setName(name);
        return n;
    }

    // Fabrique un atelier avec ses notions
    private static Workshop workshop(long id, String name, Notion... notions) {
        Workshop w = new Workshop();
        w.setId(id);
        w.setName(name);
        w.setNotions(List.of(notions));
        return w;
    }

    @Test
    void listNotionsReturnsTheNotionsAsJson() throws Exception {
        when(notionService.getAll()).thenReturn(List.of(notion("Docker")));

        mvc.perform(get("/api/notions"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"Docker\"}]",
                        JsonCompareMode.STRICT));
    }

    @Test
    void createNotionReturnsTheCreatedNotion() throws Exception {
        // Le service renvoie la notion qu'il reçoit : vérifie aussi la lecture
        // du corps JSON de la requête
        when(notionService.create(any(Notion.class)))
                .thenAnswer(call -> call.getArgument(0));

        mvc.perform(post("/api/notions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Kubernetes\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"Kubernetes\"}",
                        JsonCompareMode.STRICT));
    }

    @Test
    void createNotionWithoutNameIsRejected() throws Exception {
        // "name" est obligatoire dans la spécification : 400 attendu
        mvc.perform(post("/api/notions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listWorkshopsReturnsWorkshopsWithTheirNotions() throws Exception {
        when(workshopService.getAll())
                .thenReturn(List.of(workshop(1, "DevOps", notion("Docker"))));

        mvc.perform(get("/api/workshops"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"id\":1,\"name\":\"DevOps\","
                                + "\"notions\":[{\"name\":\"Docker\"}]}]",
                        JsonCompareMode.STRICT));
    }

    @Test
    void createWorkshopReturnsTheCreatedWorkshop() throws Exception {
        when(workshopService.create(any(Workshop.class)))
                .thenReturn(workshop(2, "CI/CD", notion("GitLab")));

        mvc.perform(post("/api/workshops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"CI/CD\","
                                + "\"notions\":[{\"name\":\"GitLab\"}]}"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{\"id\":2,\"name\":\"CI/CD\","
                                + "\"notions\":[{\"name\":\"GitLab\"}]}",
                        JsonCompareMode.STRICT));
    }

    @Test
    void showWorkshopByIdReturnsOneWorkshop() throws Exception {
        when(workshopService.getById(1))
                .thenReturn(workshop(1, "DevOps", notion("Docker")));

        mvc.perform(get("/api/workshops/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{\"id\":1,\"name\":\"DevOps\","
                                + "\"notions\":[{\"name\":\"Docker\"}]}",
                        JsonCompareMode.STRICT));
    }
}
