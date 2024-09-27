package ewm.compilation.service;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.NewCompilationDto;
import ewm.compilation.dto.UpdateCompilationRequest;
import ewm.compilation.mapper.CompilationMapper;
import ewm.compilation.model.Compilation;
import ewm.compilation.repository.CompilationRepository;
import ewm.error.exception.NotFoundException;
import ewm.event.EventRepository;
import ewm.event.model.Event;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper = CompilationMapper.INSTANCE;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        List<Event> events = Collections.emptyList();
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events = newCompilationDto.getEvents().stream()
                    .map(eventId -> eventRepository.findById(eventId)
                            .orElseThrow(() -> new EntityNotFoundException("Событие с id= " + eventId + " не найдено")))
                    .collect(Collectors.toList());
        }

        Compilation compilation = compilationMapper.newCompilationDtoToCompilation(newCompilationDto);
        compilation.setEvents(events);

        Compilation savedCompilation = compilationRepository.save(compilation);
        return compilationMapper.compilationToCompilationDto(savedCompilation);
    }

    @Override
    public List<CompilationDto> getAll(Integer from , Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Compilation> compilations = compilationRepository.findAll(pageable).stream().toList();
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

    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest updateRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException("Подборка с id= " + compId + " не найдена"));

        if (updateRequest.getEvents() != null && !updateRequest.getEvents().isEmpty()) {
            List<Event> events = updateRequest.getEvents().stream()
                    .map(eventId -> eventRepository.findById(eventId)
                            .orElseThrow(() -> new NotFoundException("Событие с id= " + eventId + " не найдено")))
                    .collect(Collectors.toList());
            compilation.setEvents(events);
        }
        if (updateRequest.getTitle() != null) {
            compilation.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getPinned() != null) {
            compilation.setPinned(updateRequest.getPinned());
        }
        Compilation savedCompilation = compilationRepository.save(compilation);
        return compilationMapper.compilationToCompilationDto(savedCompilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id= " + compId + " не найдена"));
        compilationRepository.deleteById(compId);
    }
}
