package ewm.compilation.mapper;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.NewCompilationDto;
import ewm.compilation.dto.UpdateCompilationRequest;
import ewm.compilation.model.Compilation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CompilationMapper {

    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    CompilationDto compilationToCompilationDto(Compilation compilation);

    Compilation newCompilationDtoToCompilation(NewCompilationDto newCompilationDto);

    Compilation updateCompilationFromDto(UpdateCompilationRequest updateRequest, Compilation compilation);
}
