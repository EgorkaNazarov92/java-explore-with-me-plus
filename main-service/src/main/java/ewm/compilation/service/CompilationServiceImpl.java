package ewm.compilation.service;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.NewCompilationDto;
import ewm.compilation.dto.UpdateCompilationRequest;
import ewm.compilation.mapper.CompilationMapper;
import ewm.compilation.model.Compilation;
import ewm.compilation.repository.CompilationRepository;
import ewm.error.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper = CompilationMapper.INSTANCE;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationMapper.newCompilationDtoToCompilation(newCompilationDto);
        Compilation savedCompilation = compilationRepository.save(compilation);
        return compilationMapper.compilationToCompilationDto(savedCompilation);
    }

    @Override
    public List<CompilationDto> getAll() {
        List<Compilation> compilations = compilationRepository.findAll();
        return compilations.stream()
                .map(compilationMapper::compilationToCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id= " + compId + " не найдена"));
        return compilationMapper.compilationToCompilationDto(compilation);
    }

    public CompilationDto update(Long compId, UpdateCompilationRequest updateRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException("Подборка с id= " + compId + " не найдена"));

        Compilation updatedCompilation = CompilationMapper.INSTANCE.updateCompilationFromDto(updateRequest, compilation);
        Compilation savedCompilation = compilationRepository.save(updatedCompilation);
        return CompilationMapper.INSTANCE.compilationToCompilationDto(savedCompilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id= " + compId + " не найдена"));
        compilationRepository.deleteById(compId);
    }
}
