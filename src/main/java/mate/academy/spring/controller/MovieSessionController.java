package mate.academy.spring.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import mate.academy.spring.dto.MovieSessionRequestDto;
import mate.academy.spring.dto.MovieSessionResponseDto;
import mate.academy.spring.model.MovieSession;
import mate.academy.spring.service.MovieSessionService;
import mate.academy.spring.service.impl.mapper.MovieSessionDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie-sessions")
public class MovieSessionController {
    private final MovieSessionDtoMapper movieSessionDtoMapper;
    private final MovieSessionService movieSessionService;

    @Autowired
    public MovieSessionController(MovieSessionDtoMapper movieSessionDtoMapper,
                                  MovieSessionService movieSessionService) {
        this.movieSessionDtoMapper = movieSessionDtoMapper;
        this.movieSessionService = movieSessionService;
    }

    @GetMapping("/available")
    public List<MovieSessionResponseDto> findAllAvailable(@RequestParam Long movieId,
                                                 @RequestParam @DateTimeFormat
                                                 (pattern = "dd.MM.yyyy")
                                                 LocalDate date) {
        return movieSessionService.findAvailableSessions(movieId,date)
                .stream()
                .map(movieSessionDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public MovieSessionResponseDto create(@RequestBody MovieSessionRequestDto
                                                      movieSessionRequestDto) {
        return movieSessionDtoMapper
                .toDto(movieSessionService
                        .add(movieSessionDtoMapper
                                .toModel(movieSessionRequestDto)));
    }

    @PutMapping("/{id}")
    public MovieSessionResponseDto put(@PathVariable Long id,
                                       @RequestBody MovieSessionRequestDto movieSessionRequestDto) {
        MovieSession movieSession = movieSessionDtoMapper.toModel(movieSessionRequestDto);
        movieSession.setId(id);
        return movieSessionDtoMapper.toDto(movieSessionService.update(movieSession));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        movieSessionService.delete(id);
    }
}
