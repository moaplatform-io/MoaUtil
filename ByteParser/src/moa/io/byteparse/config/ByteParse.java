package moa.io.byteparse.config;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ByteParse {

    public String name() default "no name";
    public int order() default 0;
    public int byteSize() default 0;
    public String description() default "no description";
    public boolean isByteArray() default false;
    public boolean isList() default false;
    public byte[] defaultVal() default -1; 
    public boolean isDefaultVal() default false; 
}