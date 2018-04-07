package example.com.mydiary.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by saikr on 19-03-2018.
 */
@Scope
@Retention(RetentionPolicy.CLASS)
public @interface ApplicationScope {

}
