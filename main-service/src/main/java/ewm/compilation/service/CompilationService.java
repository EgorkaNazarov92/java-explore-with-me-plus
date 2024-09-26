package ewm.compilation.service;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.NewCompilationDto;
import ewm.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    CompilationDto create(NewCompilationDto newCompilationDto);

    List<CompilationDto> getAll();

    CompilationDto getById(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequest updateRequest);

    void deleteCompilation(Long compId);
}
