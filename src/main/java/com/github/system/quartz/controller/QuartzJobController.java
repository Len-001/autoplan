package com.github.system.quartz.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.system.base.dto.AjaxResult;
import com.github.system.quartz.entity.SysQuartzJob;
import com.github.system.quartz.service.SysQuartzJobService;
import com.github.system.quartz.vo.SysQuartzJobVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@SaCheckRole("ADMIN")
@Api(tags = "定时任务")
@RestController
@RequestMapping("/admin/job")
public class QuartzJobController {

    @Resource
    private SysQuartzJobService sysQuartzJobService;


    @GetMapping("/list")
    @ApiOperation("定时任务调度list")
    public AjaxResult list(Page<SysQuartzJob> page, SysQuartzJobVo sysQuartzJobVo) {
        LambdaQueryWrapper<SysQuartzJob> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StrUtil.isNotEmpty(sysQuartzJobVo.getJobName()), SysQuartzJob::getJobName, sysQuartzJobVo.getJobName());
        lambdaQueryWrapper.eq(sysQuartzJobVo.getStatus() != null, SysQuartzJob::getStatus, sysQuartzJobVo.getStatus());
        lambdaQueryWrapper.orderByAsc(SysQuartzJob::getStatus);
        return AjaxResult.doSuccess(sysQuartzJobService.page(page, lambdaQueryWrapper));
    }

    @GetMapping("/view/{id}")
    public AjaxResult view(@PathVariable("id") String id) {
        SysQuartzJob log = sysQuartzJobService.getById(id);
        return AjaxResult.doSuccess(log);
    }

    @ApiOperation("保存")
    @PostMapping("/save")
    public AjaxResult add(@RequestBody @Validated SysQuartzJobVo sysQuartzJobVo) {
        return sysQuartzJobService.save(sysQuartzJobVo) ? AjaxResult.doSuccess() : AjaxResult.doError();
    }

    @ApiOperation("根据id删除")
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody SysQuartzJobVo sysQuartzJobVo) {
        return sysQuartzJobService.removeBatchByIds(sysQuartzJobVo.getIds()) ? AjaxResult.doSuccess() : AjaxResult.doError();
    }

    @ApiOperation("编辑")
    @PostMapping("/update")
    public AjaxResult update(@RequestBody @Validated SysQuartzJobVo sysQuartzJobVo) {
        if (sysQuartzJobVo.getId() == null) {
            return AjaxResult.doError("id不能为空");
        }
        return sysQuartzJobService.updateById(sysQuartzJobVo) ? AjaxResult.doSuccess() : AjaxResult.doError();
    }

    @ApiOperation("任务调度状态修改")
    @PostMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysQuartzJobVo sysQuartzJobVo) throws SchedulerException {
        SysQuartzJob job = sysQuartzJobService.getById(sysQuartzJobVo.getId());
        job.setStatus(job.getStatus());
        sysQuartzJobService.updateById(job);
        return AjaxResult.doSuccess(sysQuartzJobService.changeStatus(job));
    }

    /**
     * 任务调度立即执行一次
     */
    @GetMapping("/run/{id}")
    @ResponseBody
    public AjaxResult run(@PathVariable("id") String id) throws SchedulerException {
        SysQuartzJob job = sysQuartzJobService.getById(id);
        sysQuartzJobService.run(job);
        return AjaxResult.doSuccess();
    }


}