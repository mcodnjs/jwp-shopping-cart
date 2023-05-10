package cart.auth;

import cart.exception.InvalidAuthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class BasicAuthorizationExtractorTest {

    private HttpServletRequest request;
    private BasicAuthorizationExtractor basicAuthorizationExtractor;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        basicAuthorizationExtractor = new BasicAuthorizationExtractor();
    }

    @DisplayName("request의 Authorization 헤더가 null이면 InvalidBasicAuthException을 반환한다.")
    @Test
    void extract_NullAuthHeader() {
        // given
        given(request.getHeader(anyString())).willReturn(null);

        // when, then
        assertThatThrownBy(() -> basicAuthorizationExtractor.extract(request))
                .isInstanceOf(InvalidAuthException.class)
                .hasMessage("Header에 사용자 인증 정보가 존재하지 않습니다.");
    }

    @DisplayName("request의 Authorization 헤더가 Basic Type이 아니면 InvalidBasicAuthException을 반환한다.")
    @Test
    void extract_NotBasicAuthHeader() {
        // given
        given(request.getHeader(anyString())).willReturn("jamie");

        // when, then
        assertThatThrownBy(() -> basicAuthorizationExtractor.extract(request))
                .isInstanceOf(InvalidAuthException.class)
                .hasMessage("Basic 타입의 Header가 아닙니다.");
    }

    @DisplayName("request의 Authorization 헤더가 파싱할 수 없는 값이면, InvalidBasicAuthException을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"YUBhLmNvbS9wYXNzd29yZDE=", "YUBhLmNvbTo="})
    void extract_InvalidEncodingValue(String invalidEncodedString) {
        // given
        given(request.getHeader(anyString())).willReturn("Basic " + invalidEncodedString);

        // when, then
        assertThatThrownBy(() -> basicAuthorizationExtractor.extract(request))
                .isInstanceOf(InvalidAuthException.class)
                .hasMessage("유효한 Basic 인코딩 값이 아닙니다.");
    }

    @DisplayName("request의 Authorization 헤더에서 성공적으로 이메일과 패스워드를 추출하여 반환한다.")
    @Test
    void extract() {
        // given
        String validBasicAuthHeader = "Basic YUBhLmNvbTpwYXNzd29yZDE=";
        given(request.getHeader(anyString())).willReturn(validBasicAuthHeader);

        // when
        List<String> credentials = basicAuthorizationExtractor.extract(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(credentials.size()).isEqualTo(2);
            softly.assertThat(credentials.get(0)).isEqualTo("a@a.com");
            softly.assertThat(credentials.get(1)).isEqualTo("password1");
        });
    }
}
