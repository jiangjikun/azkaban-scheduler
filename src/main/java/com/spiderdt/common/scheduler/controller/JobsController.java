package com.spiderdt.common.scheduler.controller;

import com.spiderdt.common.scheduler.common.Slog;
import com.spiderdt.common.scheduler.common.Utils;
import com.spiderdt.common.scheduler.controller.InterfaceBean.JobInfoIn;
import com.spiderdt.common.scheduler.controller.InterfaceBean.JobSearchIn;
import com.spiderdt.common.scheduler.entity.JobInfosEntity;
import com.spiderdt.common.scheduler.errorhandling.AppException;
import com.spiderdt.common.scheduler.service.JobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Created by fivebit on 2017/6/16.
 * jobs 接口包括创建任务，查看任务列表，查看任务详情
 */
@Component
@Path("/jobs")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class JobsController {
    @Context
    private HttpServletRequest request;

    @Autowired
    private JobsService jobsService;
    @Autowired
    Slog slog;

    /**
     * 创建一个job
     *
     * @return
     * @throws AppException
     * @params user_id, job_id
     */
    @POST
    public Response createJob(JobInfoIn jobinfo) throws AppException {
        slog.info("create job:" + jobinfo.toString());
        jobsService.createNewJob(jobinfo);
        return Response.status(Response.Status.CREATED)// 201
                .entity(Utils.getRespons()).build();
    }


    @DELETE
    @Path("/{job_id}")
    public Response delJob(@PathParam("job_id") Integer job_id) throws AppException {
        //权限控制
        String token = request.getHeader("token");
        Boolean st = jobsService.delJobByJobId(job_id, token);
        Integer id = jobsService.createDeleteJob(job_id);
        return Response.status(Response.Status.OK)
                .entity(Utils.getRespons()).build();
    }

    /**
     * 查询任务列表。包括检索单个任务，分页，任务状态等。
     *
     * @param jobSearchIn
     * @return
     */
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    //@Consumes("text/plain; charset=UTF-8")
    public Response getJobList(@BeanParam JobSearchIn jobSearchIn) throws AppException {
        slog.info("get job list search in:" + jobSearchIn);
        Map<String, Object> job_list = jobsService.getJobList(jobSearchIn);
        return Response.status(Response.Status.OK)
                .entity(Utils.getRespons(job_list)).build();
    }

    @Path("/list")
    @POST
    public Response getJobListByPost(JobSearchIn jobSearchIn) throws AppException {
        slog.info("get job list search in:" + jobSearchIn);
        Map<String, Object> job_list = jobsService.getJobList(jobSearchIn);
        return Response.status(Response.Status.OK)
                .entity(Utils.getRespons(job_list)).build();
    }


    @PUT
    @Path("/rerun/{job_id}")
    public Response reRunJob(@PathParam("job_id") Integer job_id) throws AppException {
        //权限控制
        jobsService.rerunJobByJobId(job_id);
        return Response.status(Response.Status.OK).entity(Utils.getRespons()).build();
    }

    @GET
    @Path("/{job_id}")
    @Consumes("text/plain; charset=UTF-8")
    public Response getJobInfo(@PathParam("job_id") Integer job_id) throws AppException {
        slog.info("get job info in:" + job_id);
        JobInfosEntity info = jobsService.getJobInfoByJobId(job_id);
        return Response.status(Response.Status.OK)
                .entity(Utils.getRespons(info)).build();
    }
}
