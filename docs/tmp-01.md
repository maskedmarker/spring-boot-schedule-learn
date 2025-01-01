

## @EnableScheduling
在spring-boot项目中,需要手动在configuration-class上添加@EnableScheduling才能开启spring的定时任务.
@EnableScheduling会通过@Import(SchedulingConfiguration.class)向spring容器注册ScheduledAnnotationBeanPostProcessor

```text
@EnableScheduling

This enables detection of @Scheduled annotations on any Spring-managed bean in the container.

@Import(SchedulingConfiguration.class)
public @interface EnableScheduling {}

@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class SchedulingConfiguration {

	@Bean(name = TaskManagementConfigUtils.SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME)
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public ScheduledAnnotationBeanPostProcessor scheduledAnnotationProcessor() {
		return new ScheduledAnnotationBeanPostProcessor();
	}
}
```

### ScheduledAnnotationBeanPostProcessor
ScheduledAnnotationBeanPostProcessor会遍历spring容器中所有的bean,如果该bean包含携带@Scheduled注解的方法,这些方法都会被注册到ScheduledTaskRegistrar,并被TaskScheduler调度.

```text
ScheduledAnnotationBeanPostProcessor

Bean post-processor that registers methods annotated with @Scheduled to be invoked by a TaskScheduler according to the "fixedRate", "fixedDelay", or "cron" expression provided via the annotation.
This post-processor is automatically registered by Spring's <task:annotation-driven> XML element, and also by the @EnableScheduling annotation.
```



## auto-configure
spring的auto-configure也为scheduling提供了必要的后备基础.

```text
// 向spring容器注册scheduling所需的Executor(更准确讲是ThreadPoolTaskExecutor)
@ConditionalOnClass(ThreadPoolTaskExecutor.class)
@EnableConfigurationProperties(TaskExecutionProperties.class)
public class TaskExecutionAutoConfiguration {
    @Lazy
	@Bean(name = { APPLICATION_TASK_EXECUTOR_BEAN_NAME, AsyncAnnotationBeanPostProcessor.DEFAULT_TASK_EXECUTOR_BEAN_NAME })
	@ConditionalOnMissingBean(Executor.class) // 如果spring容器中有Executor就直接借用
	public ThreadPoolTaskExecutor applicationTaskExecutor(TaskExecutorBuilder builder) {
		return builder.build();
	}
}

// 向spring容器注册scheduling所需的TaskScheduler
@ConditionalOnClass(ThreadPoolTaskScheduler.class)
@EnableConfigurationProperties(TaskSchedulingProperties.class)
@AutoConfigureAfter(TaskExecutionAutoConfiguration.class)
public class TaskSchedulingAutoConfiguration {
    @Bean
	@ConditionalOnBean(name = TaskManagementConfigUtils.SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME) // 依赖SchedulingConfiguration引入的ScheduledAnnotationBeanPostProcessor
	@ConditionalOnMissingBean({ SchedulingConfigurer.class, TaskScheduler.class, ScheduledExecutorService.class })
	public ThreadPoolTaskScheduler taskScheduler(TaskSchedulerBuilder builder) {
		return builder.build();
	}
}
```


## cron表达式
```text
A cron-like expression, extending the usual UN*X definition to include triggers on the second, minute, hour, day of month, month, and day of week.
For example, "0 * * * * MON-FRI" means once per minute on weekdays (at the top of the minute - the 0th second).
The fields read from left to right are interpreted as follows.
    second
    minute
    hour
    day of month
    month
    day of week
The special value "-" indicates a disabled cron trigger, primarily meant for externally specified values resolved by a ${...} placeholder.
```