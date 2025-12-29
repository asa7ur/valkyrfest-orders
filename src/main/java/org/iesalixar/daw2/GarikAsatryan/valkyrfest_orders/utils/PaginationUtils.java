package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.List;

public class PaginationUtils {
    public static void setupPaginationModel(Model model, Page<?> page, Pageable pageable, String searchTerm, String activePage) {
        String sortField = "id";
        String sortDir = "ASC";

        if (pageable.getSort().isSorted()) {
            sortField = pageable.getSort().iterator().next().getProperty();
            sortDir = pageable.getSort().iterator().next().getDirection().isDescending() ? "DESC" : "ASC";
        }

        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("pageSizes", List.of(5, 10, 20, 50));
        model.addAttribute("pageSize", pageable.getPageSize());
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("ASC") ? "DESC" : "ASC");
        model.addAttribute("activePage", activePage);
    }
}
