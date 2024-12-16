# spring-boot-starter-logaspect

Spring стратер, добавляющий логирование HTTP-запросов
и ответов, а также отдельных методов.

Для логирования HTTP-запросов потребуется указать в 
application.yaml значение `true` в log.controller.enable.
Также можно указать уровень логирования в log.controller.logLevel

По мимо этого также можно осуществить логирование других методов
и сервисов при помощи аннотаций @LogBefore, @LogResult, @LogSpendTime и
@LogThrowing. Их также можно включить и отключить в application.yaml
по адресу log.annotation.enable

Дефолтно заданы значения `true` в обоих логированиях.

```
log:
  annotation:
    enable: true
  controller:
    enable: true
    logLevel: "INFO"
```