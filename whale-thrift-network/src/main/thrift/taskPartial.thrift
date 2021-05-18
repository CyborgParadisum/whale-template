
namespace java demo

struct TaskPartial {
  1: i64 taskId,       # taskid
  2: string resources # task partial resource, need execute
}

service TaskPartialService {
  string executeTask(1: TaskPartial executedTask)
}