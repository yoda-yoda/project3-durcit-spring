package org.durcit.be.follow.service.impl;

import lombok.RequiredArgsConstructor;
import org.durcit.be.follow.domain.TagFollow;
import org.durcit.be.follow.dto.TagFollowRegisterRequest;
import org.durcit.be.follow.dto.TagFollowResponse;
import org.durcit.be.follow.dto.TagFollowUpdateRequest;
import org.durcit.be.follow.repository.TagFollowRepository;
import org.durcit.be.follow.service.TagFollowService;
import org.durcit.be.security.domian.Member;
import org.durcit.be.system.exception.auth.MemberNotFoundException;
import org.durcit.be.system.exception.tagFollow.NoTagFollowInListTypeException;
import org.durcit.be.system.exception.tagFollow.TagFollowNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.durcit.be.system.exception.ExceptionMessage.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagFollowServiceImpl implements TagFollowService {



    private final TagFollowRepository tagFollowRepository;



    // 메서드 기능: 태그팔로우 저장, 삭제, 재저장, 재삭제 기능이 이 메서드 하나로 작동하도록 한다.(토글 방식)
    // 쿼리메서드를 이용한다. 태그팔로우 테이블에서 해당 멤버와 해당 태그가 일치하는 row를(엔티티를) 가져온다.
    // 있다면 delete가 true인지 false인지를 검사하여, 그 반대값으로 setter 하고 저장해준다.
    // 없다면 그냥 DB에 저장해준다.
    // 반환 : 최종 처리한 값을 응답 Dto로 변환하여 반환한다.
    @Transactional
    public TagFollowResponse createAndDeleteTagFollowByRegisterRequest(TagFollowRegisterRequest tagFollowRegisterRequest, Long memberId){


        // 어떤 멤버인지 엔티티를 획득. 해당 멤버가 없으면 오류를 던진다.
        Member member = Optional.ofNullable(tagFollowRepository.findMemberByMemberId(memberId))
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND_ERROR));

        Optional<TagFollow> optionalTagFollow = tagFollowRepository.findByMemberAndTag(member, tagFollowRegisterRequest.getTag());

        if (optionalTagFollow.isPresent()){
            if (!optionalTagFollow.get().isDeleted()){
                optionalTagFollow.get().setDeleted(true);
            } else {
                optionalTagFollow.get().setDeleted(false);
            }
            tagFollowRepository.save(optionalTagFollow.get());
            return TagFollowResponse.fromEntity(optionalTagFollow.get());
        }
        TagFollow toEntity = TagFollowRegisterRequest.toEntity(tagFollowRegisterRequest, member);
        tagFollowRepository.save(toEntity);
        return TagFollowResponse.fromEntity(toEntity);


    }




    // 메서드 기능: 유저가 추가하는 태그 내용이 담긴 요청 Dto를 List로 받는다. 그리고 그것을 각각 엔티티로 변환하여 DB에 저장하고, 해당 값을 다시 응답 Dto List로 변환하여 반환해준다.
    // 나중 추가할 로직: 이미 살아있는 태그를 똑같이 추가하면 예외가 나도록 하는것이다.
    @Transactional
    public List<TagFollowResponse> createTagFollowsByRegisterRequests(List<TagFollowRegisterRequest> tagFollowRegisterRequestList, Long memberId) {


        // 어떤 멤버인지 엔티티를 획득. 해당 멤버가 없으면 오류를 던진다.
        Member member = Optional.ofNullable(tagFollowRepository.findMemberByMemberId(memberId))
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND_ERROR));


        // 최종 반환할 타입을 미리 만들어둔다.
        List<TagFollowResponse> tagFollowResponseList = new ArrayList<>();


        // 반복자를 활용한다.
        // 요청Dto를 각각 엔티티로 바꾼후 DB에 저장한다.
        // 또한 엔티티를 다시 각각 응답Dto로 바꾼후 최종 반환 리스트에 add 한다.

        for (TagFollowRegisterRequest tagFollowRegisterRequest : tagFollowRegisterRequestList) {

            TagFollow tagFollow = TagFollowRegisterRequest.toEntity(tagFollowRegisterRequest, member);
            TagFollow savedTagFollow = tagFollowRepository.save(tagFollow);


            TagFollowResponse tagFollowResponse = TagFollowResponse.fromEntity(savedTagFollow);
            tagFollowResponseList.add(tagFollowResponse) ;

        }

        // 최종 응답 Dto List를 반환한다.
        return tagFollowResponseList;

    }




    // 메서드 기능: 업데이트 Dto를 List로 받는다. 그리고 그것을 각각 엔티티로 변환하여 DB에 저장하고, 해당 값을 다시 응답 Dto List로 변환하여 반환해준다.
    @Transactional
    public List<TagFollowResponse> createTagFollowsByUpdateRequests(List<TagFollowUpdateRequest> tagFollowUpdateRequestList, Long memberId) {


        // 어떤 멤버인지 엔티티를 획득. 해당 멤버가 없으면 오류를 던진다.
        Member member = Optional.ofNullable(tagFollowRepository.findMemberByMemberId(memberId))
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND_ERROR));


        // 최종 반환할 타입을 미리 만들어둔다.
        List<TagFollowResponse> tagFollowResponseList = new ArrayList<>();


        // 반복자를 활용한다.
        // 업데이트Dto를 각각 엔티티로 바꾼후 DB에 저장한다.
        // 또한 엔티티를 다시 각각 응답Dto로 바꾼후 최종 반환 리스트에 add 한다.

        for (TagFollowUpdateRequest tagFollowUpdateRequest : tagFollowUpdateRequestList) {

            TagFollow tagFollow = TagFollowUpdateRequest.toEntity(tagFollowUpdateRequest, member);
            TagFollow savedTagFollow = tagFollowRepository.save(tagFollow);


            TagFollowResponse tagFollowResponse = TagFollowResponse.fromEntity(savedTagFollow);
            tagFollowResponseList.add(tagFollowResponse) ;

        }

        // 최종 응답 Dto List를 반환한다.
        return tagFollowResponseList;

    }



    // 메서드 기능: 딜리트든 아니든 태그팔로우 엔티티를 전부 획득하여 엔티티 리스트를 반환한다.
    // 예외: 아예 DB가 비어있으면 오류를 던진다.
    public List<TagFollow> getAllTagFollowEntitys() {

        List<TagFollow> findAll = tagFollowRepository.findAll();

        // 아예 비어있으면 오류.
        if ( findAll.isEmpty() ) {
            throw new NoTagFollowInListTypeException(NO_TAG_FOLLOW_IN_LIST_TYPE_ERROR);
        }

        return findAll;

    }



    // 메서드 기능: 딜리트든 아니든 태그팔로우 엔티티를 전부 획득하고나서, 그것들을 다시 응답Dto 리스트로 반환해준다.
    // 예외: 아예 DB가 비어있으면 빈 리스트를 반환한다.
    public List<TagFollowResponse> getAllTagFollowResponses() {


        List<TagFollow> findAll = tagFollowRepository.findAll();


        // 아예 DB가 비어있으면 빈 리스트를 반환.
        if ( findAll.isEmpty() ) {
           return new ArrayList<>();
        }



        // 최종 반환할 응답 Dto List 변수를 미리 만들었다.
        List<TagFollowResponse> tagFollowResponseList = new ArrayList<>();



        // 각각의 TagFollow 엔티티를 응답 Dto로 변환해 List에 담아준다.
        for (TagFollow tagFollow : findAll) {
            tagFollowResponseList.add( TagFollowResponse.fromEntity(tagFollow) );
        }


        return tagFollowResponseList;

    }






    // 메서드 기능: 해당 멤버가 갖고있는 태그 엔티티를 List에 담아 획득한다.
    // deleted 상태: false 만 담는다. 가진게 전부 true면 오류를 던진다.
    // delete 든 아니든 아예 비어있으면 : 오류를 던진다.
    public List<TagFollow> getNoneDeletedTagFollows(Long memberId) {

        // 해당 pk의 멤버가 없다면 오류.
        Member member = Optional.ofNullable(tagFollowRepository.findMemberByMemberId(memberId))
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND_ERROR));

        // 해당 멤버가 가진 태그를 가져온다. 하나도 없으면 빈 리스트.
        List<TagFollow> tagFollowList = member.getTagFollowList();

        // 디비가 아예 비어있다면 오류.
        if ( tagFollowList.isEmpty() ) {
            throw new NoTagFollowInListTypeException(NO_TAG_FOLLOW_IN_LIST_TYPE_ERROR);
        }

        // deleted false 인것만 담는다. 전부 true이면 빈 리스트가된다.
        List<TagFollow> filterCollect = tagFollowList.stream()
                .filter(tagFollow -> !tagFollow.isDeleted())
                .collect(Collectors.toList());


        // 모든게 딜리트된 상태인거기 때문에 오류 발생시킨다.
        if ( filterCollect.isEmpty() ) {
            throw new NoTagFollowInListTypeException(NO_TAG_FOLLOW_IN_LIST_TYPE_ERROR);
        }

        // 최종 반환.
        return filterCollect;

    }


    //  메서드 기능: 멤버 pk로 해당 멤버가 가진 태그를 획득하고, 응답 Dto를 리스트에 담아 반환한다.
    //  deleted 상태: false 만 담는다.
    //  반환: 가진 태그가 전부 delete true이거나, 아예 태그 테이블이 비어있다면 빈 리스트를 반환한다.
    public List<TagFollowResponse> getTagFollowsResponses(Long memberId) {

        // 해당 pk의 멤버가 없다면 오류.
        Member member = Optional.ofNullable(tagFollowRepository.findMemberByMemberId(memberId))
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND_ERROR));


        // 해당 멤버가 가진 태그를 가져온다. 하나도 없으면 빈 리스트.
        List<TagFollow> tagFollowList = member.getTagFollowList();


        // 디비가 아예 비어있다면, 바로 빈 응답Dto 리스트를 반환한다.
        if ( tagFollowList.isEmpty() ) {
            return new ArrayList<>();
        }


        // deleted false 인것만 담는다. 만약 전부 true면 변수는 빈 리스트가 된다.
        List<TagFollow> filterCollect = tagFollowList.stream()
                .filter(tagFollow -> !tagFollow.isDeleted())
                .collect(Collectors.toList());


        // 변수가 비어있다면 빈 리스트를 반환한다.
        if ( filterCollect.isEmpty() ) {
            return new ArrayList<>();
        }


        // 최종 반환할 리스트를 만들어둔다.
        List<TagFollowResponse> responseList = new ArrayList<>();


        // 각 엔티티를 dto로 변환하고 최종 리스트에 담는다.
        for (TagFollow tagFollow : filterCollect) {

            TagFollowResponse tagFollowResponse = TagFollowResponse.fromEntity(tagFollow);
            responseList.add(tagFollowResponse);
        }


        // 최종 반환.
        return responseList;

    }



    // 메서드 기능: 유저가 기존 태그를 수정한다.
    @Transactional
    public List<TagFollowResponse> updateTagFollows(List<TagFollowUpdateRequest> tagFollowUpdateRequestList, Long memberId ) {

        // 내부 메서드를 활용하여 가진 태그를 소프트딜리트 처리한다.
        // 기존에 아무태그를 안갖고 있었거나, 이미 전부가 delete 처리된 상태였다면 이 메서드 내부에서 예외가 나온다.
        deleteNoneDeletedAllTagFollows(memberId);

        // 내부 메서드를 활용했다. 업데이트 dto 리스트와 memberId를 통해 저장한다.
        // return값도 밑 메서드와 같기때문에 바로 return 해준다.
        return createTagFollowsByUpdateRequests(tagFollowUpdateRequestList, memberId);

    }




    // 메서드 기능: 해당 유저가 기존에 갖고 있던 태그를 전부 소프트딜리트 처리 한다.
    // 예외: 기존에 아무 태그를 안갖고 있었거나, 이미 전부가 delete 처리된 상태였다면 예외 처리한다.
    @Transactional
    public void deleteNoneDeletedAllTagFollows(Long memberId) {


        // 해당 멤버를 찾는다. 없으면 오류를 던진다.
        Member member = Optional.ofNullable(tagFollowRepository.findMemberByMemberId(memberId))
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND_ERROR));


        // 해당 멤버가 가진 태그를 찾는다.
        List<TagFollow> tagFollowList = member.getTagFollowList();


        // 아무 태그도 없다면 오류를 던진다.
        if ( tagFollowList.isEmpty() ) {
            throw new NoTagFollowInListTypeException(NO_TAG_FOLLOW_IN_LIST_TYPE_ERROR);
        }

        // 이미 소프트 딜리트 처리된것은 뺀다.
        List<TagFollow> filterCollect = tagFollowList.stream()
                .filter(tagFollow -> !tagFollow.isDeleted())
                .collect(Collectors.toList());


        // 이미 모든게 소프트 딜리트 처리된 상태면 오류를 던진다.
        if ( filterCollect.isEmpty() ) {
            throw new NoTagFollowInListTypeException(NO_TAG_FOLLOW_IN_LIST_TYPE_ERROR);
        }

        // 보유한 태그를 모두 소프트 딜리트 처리한다.
        // 일부가 이미 딜리트 상태여도 똑같이 딜리트 처리된다.
        for (TagFollow tagFollow : filterCollect) {
            tagFollow.setDeleted(true);
            tagFollowRepository.save(tagFollow);
        }

    }

    // 메서드 기능: 태그팔로우의 id를 List에 담아 전부 소프트 딜리트 처리한다.
    @Transactional
    public void deleteAllTagFollowsByIds(List<Long> tagFollowIdList) {


        // 반복자를 이용해 각 id의 태그를 조회하고 모두 소프트 딜리트 처리한다.
        // 혹시 이미 되어있더라도 그냥 처리된다.
        for (Long l : tagFollowIdList) {
            TagFollow tagFollow = tagFollowRepository.findById(l)
                    .orElseThrow(() -> new TagFollowNotFoundException(TAG_FOLLOW_NOT_FOUND_ERROR));
            tagFollow.setDeleted(true);
            tagFollowRepository.save(tagFollow);
        }

    }

    // 메서드 기능: 태그팔로우 id를 명시적으로 1개만 받아와서 1개의 태그팔로우를 소프트 딜리트 처리한다.
    @Transactional
    public void deleteOneTagFollowById(Long tagFollowId) {


        // 해당 id의 태그를 1개 조회하고 소프트 딜리트 처리한다.
        // 혹시 이미 되어있더라도 그냥 처리된다.
        TagFollow tagFollow = tagFollowRepository.findById(tagFollowId)
                .orElseThrow(() -> new TagFollowNotFoundException(TAG_FOLLOW_NOT_FOUND_ERROR));
        tagFollow.setDeleted(true);
        tagFollowRepository.save(tagFollow);

    }






}




















