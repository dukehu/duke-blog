# duke-blog
博客服务


### 使用lombok，让实体类变得更加简单
> https://blog.csdn.net/motui/article/details/79012846
#### Lombok有一下注解，在对应的类或者方法上使用对应注解即可。
> * @Setter/@Getter  
> * @Data
> * @Log(这是一个泛型注解，具体有很多种形式), 如：@Slf4j,就相当于是在此类写了  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogExample.class);
> * @AllArgsConstructor 该注解使用在类上，该注解提供一个全参数的构造方法，默认不提供无参构造。
> * @NoArgsConstructor 该注解使用在类上，该注解提供一个无参构造 
> * @EqualsAndHashCode
> * @NonNull
> * @Cleanup
> * @ToString
> * @RequiredArgsConstructor
> * @Value
> * @SneakyThrows
> * @Synchronized 
