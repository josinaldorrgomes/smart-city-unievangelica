package br.edu.unievangelica.smartcity.controller;

import br.edu.unievangelica.smartcity.model.IoTModule;
import br.edu.unievangelica.smartcity.model.ModuleType;
import br.edu.unievangelica.smartcity.service.IoTModuleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/modules")
public class ModuleAdminController {

    private final IoTModuleService moduleService;

    public ModuleAdminController(IoTModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("modules", moduleService.findAll());
        return "admin/modules-list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("module", new IoTModule());
        model.addAttribute("types", ModuleType.values());
        model.addAttribute("editing", false);
        return "admin/module-form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("module", moduleService.findByIdOrThrow(id));
        model.addAttribute("types", ModuleType.values());
        model.addAttribute("editing", true);
        return "admin/module-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("module") IoTModule module,
                        BindingResult bindingResult,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("types", ModuleType.values());
            model.addAttribute("editing", module.getId() != null);
            return "admin/module-form";
        }

        boolean isNew = module.getId() == null;
        moduleService.save(module);
        redirectAttributes.addFlashAttribute("mensagem",
                isNew ? "Módulo IoT cadastrado com sucesso!" : "Módulo IoT atualizado com sucesso!");
        return "redirect:/admin/modules";
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        moduleService.toggleActive(id);
        redirectAttributes.addFlashAttribute("mensagem", "Status do módulo atualizado.");
        return "redirect:/admin/modules";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        moduleService.delete(id);
        redirectAttributes.addFlashAttribute("mensagem", "Módulo IoT removido com sucesso.");
        return "redirect:/admin/modules";
    }
}
