package br.com.api.habitFlow.dto;

import br.com.api.habitFlow.model.habito.Frequency;
import br.com.api.habitFlow.model.habito.Habito;
import br.com.api.habitFlow.model.habito.Unit;

public record DadosListagemHabito(
        Long id,
        String name,
        String description,
        Frequency frequency,
        Integer target,
        Unit unit,
        Boolean active
) {
    public DadosListagemHabito(Habito habito) {
        this(habito.getId(), habito.getName(), habito.getDescription(), habito.getFrequency(), habito.getTarget(), habito.getUnit(), habito.getActive());
    }
}
