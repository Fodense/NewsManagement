package by.brel.newsmanagement.exception_handling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class DefaultResponseData; With fields uri and info
 * Use it for response on DELETE request and Exception Handling
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultResponseData {
    private String uri;
    private String info;
}
