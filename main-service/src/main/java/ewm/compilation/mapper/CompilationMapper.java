package ewm.compilation.mapper;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.NewCompilationDto;
import ewm.compilation.model.Compilation;
import ewm.event.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CompilationMapper {

    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    CompilationDto compilationToCompilationDto(Compilation compilation);
    Compilation newCompilationDtoToCompilation(NewCompilationDto newCompilationDto);
    List<Event> map(List<Long> eventIds);
    Event map(Long eventId);
}
