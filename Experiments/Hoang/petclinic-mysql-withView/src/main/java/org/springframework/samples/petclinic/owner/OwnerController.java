/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @ModifiedBy Tanmay Ghosh
 */
@Controller
class OwnerController {

    @Autowired
    private OwnerRepository ownersRepository;

    private final Logger logger = LoggerFactory.getLogger(OwnerController.class);

    @GetMapping("/owners/new")
    public String initCreationForm(Map<String, Object> model) {
        logger.info("Entered into Controller Layer");
        Owners owner = new Owners();
        model.put("owner", owner);
        return "owners/createOrUpdateOwnerForm";
    }

    @PostMapping("/owners/new")
    public String processCreationForm(@Valid Owners owner, BindingResult result) {
        if (result.hasErrors()) {
            return "owners/createOrUpdateOwnerForm";
        } else {
            ownersRepository.save(owner);
            return "redirect:/owners";
        }
    }

    @GetMapping("/owners")
    public String getAllOwners(Map<String, Object> model) {

        logger.info("Entered into Controller Layer");
        Collection<Owners> results = ownersRepository.findAll();
        logger.info("Number of Records Fetched:" + results.size());
        model.put("selections", results);
        return "owners/ownersList";
    }

    @GetMapping("/owners/{ownerId}")
    public String findOwnerById(@PathVariable("ownerId") int id, Map<String, Object> model) {

        logger.info("Entered into Controller Layer");
        Collection<Owners> results = ownersRepository.findById(id);
        logger.info("Number of Records Fetched:" + results.size());
        model.put("selections", results);
        return "owners/ownersList";
    }

    @GetMapping("/owners/find")
    public String findOwner(Map<String, Object> model) {
        model.put("owner", new Owners());
        return "owners/findOwners";
    }

}
